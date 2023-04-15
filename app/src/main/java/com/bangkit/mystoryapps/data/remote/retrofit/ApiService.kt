package com.bangkit.mystoryapps.data.remote.retrofit

import com.bangkit.mystoryapps.data.remote.response.DetailStoryResponse
import com.bangkit.mystoryapps.data.remote.response.LoginResponse
import com.bangkit.mystoryapps.data.remote.response.RegisterResponse
import com.bangkit.mystoryapps.data.remote.response.StoryResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("stories")
    suspend fun getStories(): Response<StoryResponse>

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String
    ): Response<DetailStoryResponse>
}