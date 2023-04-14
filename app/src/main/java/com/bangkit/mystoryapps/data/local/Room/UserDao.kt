package com.bangkit.mystoryapps.data.local.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.mystoryapps.data.local.Entity.RegisEntity
import com.bangkit.mystoryapps.data.local.Entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM regis_results")
    fun getRegis(): LiveData<RegisEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = RegisEntity::class)
    fun insertRegis(regisEntity: RegisEntity)

    @Query("DELETE FROM regis_results")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = UserEntity::class)
    fun insertLogin(userEntity: UserEntity)
}