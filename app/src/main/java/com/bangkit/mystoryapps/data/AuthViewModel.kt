package com.bangkit.mystoryapps.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.mystoryapps.data.local.Entity.RegisEntity

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun regisUser(username: String, email: String, password: String) = userRepository.register(username, email, password)

    fun loginUser(email: String, password: String) = userRepository.login(email, password)
}