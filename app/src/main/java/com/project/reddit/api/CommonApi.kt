package com.project.reddit.api

import com.project.reddit.entity.SubredditCommon
import com.project.reddit.entity.User
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.entity.UserActivityPostData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/subreddits/popular")
    suspend fun getPopularSubreddits(
        @Header("Authorization") accessToken: String,
        @Query("limit") limit: Int
    ): SubredditCommon

    @GET("/subreddits/new")
    suspend fun getNewSubreddits(
        @Header("Authorization") accessToken: String,
        @Query("limit") limit: Int
    ): SubredditCommon

    @GET("/subreddits/search")
    suspend fun searchSubreddits(
        @Header("Authorization") accessToken: String,
        @Query("limit") limit: Int,
        @Query("q") query: String
    ): SubredditCommon


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