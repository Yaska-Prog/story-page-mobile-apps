package com.bangkit.mystoryapps.data

sealed class Result<out R> private constructor() { //agar hasil yang ditampilkan selaras
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String): Result<Nothing>()
    object Loading : Result<Nothing>()
}