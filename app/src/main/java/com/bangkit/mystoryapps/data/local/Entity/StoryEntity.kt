package com.bangkit.mystoryapps.data.local.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")
data class StoryEntity (
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String,
    var description: String,
    var photoUrl: String,
    var createdAt: String,
    var latitude: String?,
    var longitude: String?
)