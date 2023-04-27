package com.bangkit.mystoryapps.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.remote.response.LoginResponse
import com.bangkit.mystoryapps.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
){
    fun register(username: String, email: String, password: String): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.regisUser(username, email, password)
            if(client.isSuccessful && client.body() != null){
                val responseBody = client.body()
                val message = responseBody!!.message

                emit(Result.Success("Message: $message"))
            }
            else if(client.code() == 400) {
                emit(Result.Error("Email already exists, message: ${client.message()}"))
            }
            else{
                emit(Result.Error("Bad authentication, message: ${client.message()}"))
            }
        } catch (e: Exception){
            emit(Result.Error("Retrofit failed, message: ${e.message}"))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try{
            val client = apiService.loginUser(email, password)
            if (client.isSuccessful && client.body() != null) {
                val responseBody = client.body()
                if (responseBody?.error as Boolean) {
                    emit(Result.Error(responseBody.message))
                } else {
                    emit(Result.Success(responseBody))
                }
            }
            else {
                emit(Result.Error(client.message()))
            }
        } catch (e: Exception){
            emit(Result.Error("Retrofit failed, message: ${e.message}"))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}