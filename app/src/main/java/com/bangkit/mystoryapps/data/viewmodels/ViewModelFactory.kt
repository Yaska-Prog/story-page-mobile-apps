package com.bangkit.mystoryapps.data.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.mystoryapps.data.Injection
import com.bangkit.mystoryapps.data.repositories.StoryRepository
import com.bangkit.mystoryapps.data.repositories.UserRepository

class ViewModelFactory(private val userRepository: UserRepository? = null, private val storyRepository: StoryRepository? = null) : ViewModelProvider.NewInstanceFactory()  {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userRepository as UserRepository) as T
        }
        else if (modelClass.isAssignableFrom(StoryViewModel::class.java)){
            return StoryViewModel(storyRepository as StoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getUserInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(userRepository = Injection.provideUserRepository(context))
            }.also { instance = it }

        @Volatile
        private var storyInstance: ViewModelFactory? = null
        fun getStoryInstance(context: Context): ViewModelFactory =
            storyInstance ?: synchronized(this){
                storyInstance ?: ViewModelFactory(storyRepository = Injection.provideStoryRepository(context))
            }.also { storyInstance = it }
    }
}