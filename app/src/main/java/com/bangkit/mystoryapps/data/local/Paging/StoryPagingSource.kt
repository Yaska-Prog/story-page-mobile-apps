package com.bangkit.mystoryapps.data.local.Paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.remote.response.ListStoryItem
import com.bangkit.mystoryapps.data.remote.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val client = apiService.getStoriesPaging(page = position, size = params.loadSize)
            var responseData: List<ListStoryItem>? = null
            if(client.isSuccessful && client.body() != null){
                val body = client.body()
                responseData = body!!.listStory
            }

            LoadResult.Page(
                data = responseData as List<ListStoryItem>,
                prevKey = if(position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if(responseData.isNullOrEmpty()) null else position +1
            )
        } catch (exception: Exception){
            return LoadResult.Error(exception)
        }
    }

    companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}