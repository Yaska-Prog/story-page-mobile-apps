package com.bangkit.mystoryapps.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.local.Entity.StoryEntity
import com.bangkit.mystoryapps.data.repositories.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepo: StoryRepository): ViewModel() {

    fun storyPaging(): LiveData<PagingData<StoryEntity>> = storyRepo.getStoryPaging().cachedIn(viewModelScope)

    fun getDetailStory(id: String) = storyRepo.getDetailStory(id)

    fun addStory(image: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?) = storyRepo.uploadImage(image, description, lat, lon)

    fun getAllStoryList(): LiveData<Result<List<StoryEntity>>> = storyRepo.getAllStory()
}