package com.bangkit.mystoryapps.data

import android.content.Context
import com.bangkit.mystoryapps.data.local.SharedPreferenceManager
import com.bangkit.mystoryapps.data.local.database.MyStoryDatabase
import com.bangkit.mystoryapps.data.remote.retrofit.ApiConfig
import com.bangkit.mystoryapps.data.repositories.StoryRepository
import com.bangkit.mystoryapps.data.repositories.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val sharedPref = SharedPreferenceManager(context)
        val apiService = ApiConfig(sharedPref).getApiService()
        return UserRepository.getInstance(apiService)
    }

    fun provideStoryRepository(context: Context): StoryRepository{
        val sharedPref = SharedPreferenceManager(context)
        val apiService = ApiConfig(sharedPref).getApiService()
        val database = MyStoryDatabase.getDatabase(context)
        val dao = database.storyDao()
        return StoryRepository.getInstance(apiService, dao, database)
    }
}