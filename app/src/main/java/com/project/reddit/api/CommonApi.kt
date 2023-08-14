package com.project.reddit.api

import com.project.reddit.entity.CommentCommon
import com.project.reddit.entity.PostCommon
import com.project.reddit.entity.SubredditCommon
import com.project.reddit.entity.SubredditData
import com.project.reddit.entity.User
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.entity.UserActivityPostData
import com.project.reddit.entity.UserData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val COMMON_URL = "https://oauth.reddit.com"

interface CommonApi {

    @GET("/api/v1/me")
    suspend fun getUser(
        @Header("Authorization") accessToken: String
    ): User

    @GET("/user/{userAlias}/about")
    suspend fun getUserInfo(
        @Header("Authorization") accessToken: String,
        @Path("userAlias") userAlias: String
    ): UserData

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

    @GET("/r/{subreddit}/about")
    suspend fun getSubredditInfo(
        @Header("Authorization") accessToken: String,
        @Path("subreddit") subreddit: String
    ): SubredditData

    @GET("/r/{subreddit}")
    suspend fun getSubredditTopics(
        @Header("Authorization") accessToken: String,
        @Path("subreddit") subreddit: String,
        @Query("limit") limit: Int
    ): PostCommon

    @GET("/r/{subreddit}/comments/{commentsId}/{post}/")
    suspend fun getTopicComments(
        @Header("Authorization") accessToken: String,
        @Path("subreddit") subreddit: String,
        @Path("commentsId") commentsId: String,
        @Path("post") post: String
    ): List<CommentCommon>

    @POST("/api/save")
    suspend fun saveComment(
        @Header("Authorization") accessToken: String,
        @Query("id") commentId: String
    )

    @POST("/api/unsave")
    suspend fun unsaveComment(
        @Header("Authorization") accessToken: String,
        @Query("id") commentId: String
    )

    @POST("/api/vote")
    suspend fun voteComment(
        @Header("Authorization") accessToken: String,
        @Query("id") commentId: String,
        @Query("dir") voteDir: String
    )

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