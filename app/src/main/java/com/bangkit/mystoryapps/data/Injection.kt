package com.bangkit.mystoryapps.data

import android.content.Context
import android.util.Log
import com.bangkit.mystoryapps.data.local.Room.MainDatabase
import com.bangkit.mystoryapps.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository{
        val apiService = ApiConfig.getApiService()
        val database = MainDatabase.getDatabase(context)
        val dao = database.userDao()
        return UserRepository.getInstance(apiService, dao)
    }
}