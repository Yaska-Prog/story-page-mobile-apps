package com.bangkit.mystoryapps.data.local.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity(
    @field:ColumnInfo(name="userId")
    @field:PrimaryKey(autoGenerate = false)
    var userId: String,

    @field:ColumnInfo(name = "username")
    var username: String,

    @field:ColumnInfo(name = "token")
    var token: String
)