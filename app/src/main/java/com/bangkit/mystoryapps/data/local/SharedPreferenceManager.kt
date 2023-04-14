package com.bangkit.mystoryapps.data.local

import android.content.Context
import com.bangkit.mystoryapps.data.local.Entity.UserEntity

class SharedPreferenceManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("loginPref", Context.MODE_PRIVATE)

    fun saveUser(userEntity: UserEntity){
        sharedPreferences.edit()
            .putString("userId", userEntity.userId)
            .putString("name", userEntity.username)
            .putString("token", userEntity.token)
            .apply()
    }

    fun getUser(): UserEntity? {
        val userId = sharedPreferences.getString("userId", null)
        val name = sharedPreferences.getString("name", null)
        val token = sharedPreferences.getString("token", null)
        if(userId != null && name != null && token != null){
            return UserEntity(userId, name, token)
        }
        else{
            return null
        }
    }

    fun clear(){
        sharedPreferences.edit().clear().apply()
    }
}