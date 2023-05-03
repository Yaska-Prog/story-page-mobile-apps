package com.bangkit.mystoryapps.data.viewmodels

import androidx.lifecycle.ViewModel
import com.bangkit.mystoryapps.data.repositories.UserRepository

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun regisUser(username: String, email: String, password: String) = userRepository.register(username, email, password)
    fun loginUser(email: String, password: String) = userRepository.login(email, password)
}