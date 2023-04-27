package com.bangkit.mystoryapps.data.remote.retrofit

import com.bangkit.mystoryapps.data.local.SharedPreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val sharedPref: SharedPreferenceManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val start = chain.request()
        val token = sharedPref.getUser()?.token
        return if(!token.isNullOrEmpty()){
            val interceptor = start.newBuilder()
                .addHeader("Authorization", "bearer $token")
                .build()
            chain.proceed(interceptor)
        } else{
            chain.proceed(start)
        }
    }
}