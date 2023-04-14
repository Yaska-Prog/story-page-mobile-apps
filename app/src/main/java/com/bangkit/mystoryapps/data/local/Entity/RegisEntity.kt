package com.bangkit.mystoryapps.data.local.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "regis_results")
class RegisEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "error")
    var error: Boolean? = null,

    @ColumnInfo(name = "message")
    var message: String? = null
)