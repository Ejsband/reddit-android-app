package com.project.reddit.api

import com.project.reddit.entity.User
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.entity.UserActivityPostData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

const val COMMON_URL = "https://oauth.reddit.com"

interface CommonApi {

    @GET("/api/v1/me")
    suspend fun getUser(
        @Header("Authorization") accessToken: String
    ): User

    @GET("/user/{userAlias}/submitted")
    suspend fun getUserActivityPosts(
        @Header("Authorization") accessToken: String,
        @Path("userAlias") userAlias: String
        ): UserActivityPostData

    @GET("/user/{userAlias}/saved")
    suspend fun getUserActivitySavedPosts(
        @Header("Authorization") accessToken: String,
        @Path("userAlias") userAlias: String
    ): UserActivityPostData

    @GET("/user/{userAlias}/comments")
    suspend fun getUserActivityComments(
        @Header("Authorization") accessToken: String,
        @Path("userAlias") userAlias: String
    ): UserActivityCommentData

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