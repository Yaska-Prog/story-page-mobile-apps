package com.bangkit.mystoryapps.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.remote.response.AddStoryResponse
import com.bangkit.mystoryapps.data.remote.response.ListStoryItem
import com.bangkit.mystoryapps.data.remote.response.Story
import com.bangkit.mystoryapps.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
){
    fun getStories(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.getStories()
            if(client.isSuccessful && client.body() != null){
                val responseBody = client.body()
                val story = responseBody?.listStory as List<ListStoryItem>
                emit(Result.Success(story))
            }
            else{
                emit(Result.Error("Error: ${client.errorBody()}"))
            }
        } catch (e: Exception){
            emit(Result.Error("Retrofit failed, message: ${e.message}"))
        }
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

    fun uploadImage(image: MultipartBody.Part, description: RequestBody): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.uploadStory(image, description)
            if(client.isSuccessful && client.body() != null){
                val responseBody = client.body()
                emit(Result.Success(responseBody!!))
            }
            else{
                emit(Result.Error("File yang akan di upload mengalami error, silahkan cek kembali file nya dan pastikan ukurannya tidak lebih dari 1 mb."))
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
        ): StoryRepository =
            instance ?: synchronized(this){
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}