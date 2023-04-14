package com.bangkit.mystoryapps.data.remote.retrofit

import com.bangkit.mystoryapps.data.remote.response.LoginResponse
import com.bangkit.mystoryapps.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun regisUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
//    @Body user: RegisBody
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>
}