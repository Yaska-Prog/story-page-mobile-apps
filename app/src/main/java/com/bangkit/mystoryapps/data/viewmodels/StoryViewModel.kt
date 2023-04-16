package com.bangkit.mystoryapps.data.viewmodels

import androidx.lifecycle.ViewModel
import com.bangkit.mystoryapps.data.repositories.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepo: StoryRepository): ViewModel() {
    fun getStories() = storyRepo.getStories()

    fun getDetailStory(id: String) = storyRepo.getDetailStory(id)

    fun addStory(image: MultipartBody.Part, description: RequestBody) = storyRepo.uploadImage(image, description)
}