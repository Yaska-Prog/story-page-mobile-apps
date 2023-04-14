package com.bangkit.mystoryapps.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.mystoryapps.data.local.Room.UserDao
import com.bangkit.mystoryapps.data.remote.response.LoginResponse
import com.bangkit.mystoryapps.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
){
    //apabila menggunakan live data tidak perlu di suspend, karena tujuan dari suspend adalah agar data yang dikirimkan berupa list yang dikirimkan terus menerus oleh karena itu karena livedata secara tidak langsung mengirimkan data secara terus menerus maka tidak diperlukan lagi suspend
    fun register(username: String, email: String, password: String): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.regisUser(username, email, password)
            if(client.isSuccessful && client.body() != null){
                val responseBody = client.body()
                val message = responseBody!!.message

                emit(com.bangkit.mystoryapps.data.Result.Success("Message: $message"))
            }
            else if(client.code() == 400) {
                emit(com.bangkit.mystoryapps.data.Result.Error("Email already exists, message: ${client.message()}"))
            }
            else{
                emit(com.bangkit.mystoryapps.data.Result.Error("Bad authentication, message: ${client.message()}"))
            }
        } catch (e: Exception){
            emit(com.bangkit.mystoryapps.data.Result.Error("Retrofit failed, message: ${e.message}"))
        }
    }

    fun getRegisData() = userDao.getRegis()

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try{
            val client = apiService.loginUser(email, password)
            if (client.isSuccessful && client.body() != null) {
                val responseBody = client.body()
                if (responseBody?.error as Boolean) {
                    emit(com.bangkit.mystoryapps.data.Result.Error(responseBody.message))
                } else {
                    emit(com.bangkit.mystoryapps.data.Result.Success(responseBody))
                }
            }
            else {
                emit(com.bangkit.mystoryapps.data.Result.Error(client.message()))
            }
        } catch (e: Exception){
            emit(com.bangkit.mystoryapps.data.Result.Error("Retrofit failed, message: ${e.message}"))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
        ): UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(apiService, userDao)
            }.also { instance = it }
    }
}