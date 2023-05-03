package com.bangkit.mystoryapps.data.local.database

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bangkit.mystoryapps.data.local.Entity.RemoteKeysEntity
import com.bangkit.mystoryapps.data.local.Entity.StoryEntity
import com.bangkit.mystoryapps.data.remote.response.ListStoryItem
import com.bangkit.mystoryapps.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: MyStoryDatabase,
    private val apiService: ApiService
): RemoteMediator<Int, StoryEntity>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull{it.data.isNotEmpty()}?.data?.lastOrNull()?.let { data ->
            database.remoteDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull{it.data.isNotEmpty()}?.data?.firstOrNull()?.let { data ->
            database.remoteDao().getRemoteKeysId(id = data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCourrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteDao().getRemoteKeysId(id)
            }
        }
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCourrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val client = apiService.getStoriesPaging(page = page, size = state.config.pageSize)
            var responseData: List<ListStoryItem>? = null
            if(client.isSuccessful && client.body() != null){
                val body = client.body()
                responseData = body!!.listStory
            }
            val endOfPaginationReached = responseData?.isEmpty()
            val listStory = responseData?.map { stories ->
                StoryEntity(
                    stories.id,
                    stories.name,
                    stories.description,
                    stories.photoUrl,
                    stories.createdAt,
                    stories.lat.toString(),
                    stories.lon.toString()
                )
            }
            database.withTransaction {
                if(loadType == LoadType.REFRESH){
                    database.remoteDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }
                val prevKey = if(page == 1) null else page -1
                val nextKey = if (endOfPaginationReached as Boolean) null else page +1
                val keys = listStory?.map {
                    RemoteKeysEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteDao().insertAll(keys as List<RemoteKeysEntity>)
                database.storyDao().insertStory(listStory)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached as Boolean)
        } catch (exception: Exception){
            return MediatorResult.Error(exception)
        }
    }

    companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}