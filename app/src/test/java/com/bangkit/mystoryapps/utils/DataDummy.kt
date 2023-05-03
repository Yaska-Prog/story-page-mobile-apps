package com.bangkit.mystoryapps.utils

import com.bangkit.mystoryapps.data.local.Entity.StoryEntity

object DataDummy {
    fun generateDummyStoryEntity(): ArrayList<StoryEntity>{
        val storyList = ArrayList<StoryEntity>()
        for(i in 0..15){
            val story = StoryEntity(
                "story$i",
                "Story ke $i",
                "Ini deskripsi Story ke $i",
                "https://todaysparent.mblycdn.com/tp/resized/2017/06/1200x630/when-your-kid-becomes-a-meme.jpg",
                "2022-01-08T06:34:18.598Z",
                "-10.212",
                "-16.002"
            )
            storyList.add(story)
        }
        return storyList
    }

}