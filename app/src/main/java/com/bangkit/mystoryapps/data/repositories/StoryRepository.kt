package com.bangkit.mystoryapps.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.local.Entity.StoryEntity
import com.bangkit.mystoryapps.data.local.database.MyStoryDao
import com.bangkit.mystoryapps.data.local.database.MyStoryDatabase
import com.bangkit.mystoryapps.data.local.database.StoryRemoteMediator
import com.bangkit.mystoryapps.data.remote.response.AddStoryResponse
import com.bangkit.mystoryapps.data.remote.response.ListStoryItem
import com.bangkit.mystoryapps.data.remote.response.Story
import com.bangkit.mystoryapps.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val storyDao: MyStoryDao,
    private val storyDatabase: MyStoryDatabase
){
    fun getAllStory(): LiveData<Result<List<StoryEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.getStories()
            if(client.isSuccessful && client.body() != null){
                val body = client.body()
                val data = body!!.listStory
                val listStory = data.map { stories ->
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
                emit(Result.Success(listStory))
            }
            else{
                emit(Result.Error("Retrofit failed to get data"))
            }
        } catch (exception: Exception){
            emit(Result.Error("Retrofit failed, message: ${exception.message}"))
        }
    }
    fun getStoryPaging(): LiveData<PagingData<StoryEntity>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
    fun getDetailStory(id: String): LiveData<Result<Story>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.getStoryDetail(id)
            if(client.isSuccessful && client.body() != null){
                val responseBody = client.body()!!
                val story = responseBody.story
                emit(Result.Success(story))
            }
            else{
                emit(Result.Error("Error: ${client.errorBody()}"))
            }
        } catch (e: Exception){
            emit(Result.Error("Retrofit error, message: ${e.message}"))
        }
    }

    fun uploadImage(image: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.uploadStory(image, description, lat, lon)
            if(client.isSuccessful && client.body() != null){
                val responseBody = client.body()
                emit(Result.Success(responseBody!!))
            }
            else{
                emit(Result.Error("File yang akan di upload mengalami error, silahkan coba upload kembali karena map terkadang gagal mengambil lokasi, harap upload kembali."))
            }
        } catch (e: Exception){
            emit(Result.Error("Retrofit Error, message: ${e.message}"))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            storyDao: MyStoryDao,
            storyDatabase: MyStoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this){
                instance ?: StoryRepository(apiService, storyDao, storyDatabase)
            }.also { instance = it }
    }
}