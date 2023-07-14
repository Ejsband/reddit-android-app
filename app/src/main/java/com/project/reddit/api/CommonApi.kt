package com.project.reddit.api

import com.project.reddit.entity.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

const val COMMON_URL = "https://oauth.reddit.com"

interface CommonApi {

    @GET("/api/v1/me")
    suspend fun getUser(
        @Header("Authorization") accessToken: String
    ): User

    companion object {
        fun create(): CommonApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(COMMON_URL)
                .build()
            return retrofit.create(CommonApi::class.java)
        }
    }
}