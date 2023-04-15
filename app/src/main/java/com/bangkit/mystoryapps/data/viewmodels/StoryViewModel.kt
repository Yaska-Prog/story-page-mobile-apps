package com.bangkit.mystoryapps.data.viewmodels

import androidx.lifecycle.ViewModel
import com.bangkit.mystoryapps.data.repositories.StoryRepository

class StoryViewModel(private val storyRepo: StoryRepository): ViewModel() {
    fun getStories() = storyRepo.getStories()

    fun getDetailStory(id: String) = storyRepo.getDetailStory(id)
}