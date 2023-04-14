package com.bangkit.mystoryapps.data.local.Room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkit.mystoryapps.data.local.Entity.RegisEntity
import com.bangkit.mystoryapps.data.local.Entity.UserEntity

@Database(entities = [RegisEntity::class, UserEntity::class], version = 1)
abstract class MainDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE: MainDatabase? = null
        fun getDatabase(context: Context): MainDatabase{
            if(INSTANCE == null){
                synchronized(MainDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, MainDatabase::class.java, "user_database").allowMainThreadQueries().build()
                }
            }
            return INSTANCE as MainDatabase
        }
    }
}