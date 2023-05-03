package com.bangkit.mystoryapps.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkit.mystoryapps.data.local.Entity.RemoteKeysEntity
import com.bangkit.mystoryapps.data.local.Entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = true)
abstract class MyStoryDatabase: RoomDatabase() {
    abstract fun storyDao(): MyStoryDao
    abstract fun remoteDao(): RemoteKeysDao

    companion object{
        @Volatile
        private var INSTANCE: MyStoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): MyStoryDatabase {
            if(INSTANCE == null){
                synchronized(MyStoryDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        MyStoryDatabase::class.java, "story_database")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as MyStoryDatabase
        }
    }
}