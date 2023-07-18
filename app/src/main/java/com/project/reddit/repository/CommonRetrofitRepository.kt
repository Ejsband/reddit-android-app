package com.project.reddit.repository

import com.project.reddit.api.CommonApi
import com.project.reddit.entity.SubredditCommon
import com.project.reddit.entity.User
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.entity.UserActivityPostData
import javax.inject.Inject

class CommonRetrofitRepository @Inject constructor() {

    private val commonApi = CommonApi.create()

    suspend fun getUser(accessToken: String): User {
        return commonApi.getUser(accessToken)
    }

    suspend fun getUserActivityPosts(accessToken: String, userAlias: String): UserActivityPostData {
        return commonApi.getUserActivityPosts(accessToken, userAlias)
    }

    suspend fun getUserActivitySavedPosts(accessToken: String, userAlias: String): UserActivityPostData {
        return commonApi.getUserActivitySavedPosts(accessToken, userAlias)
    }

    suspend fun getUserActivityComments(accessToken: String, userAlias: String): UserActivityCommentData {
        return commonApi.getUserActivityComments(accessToken, userAlias)
    }

    suspend fun getPopularSubreddits(accessToken: String, limit: Int): SubredditCommon {
        return commonApi.getPopularSubreddits(accessToken, limit)
    }

    suspend fun getNewSubreddits(accessToken: String, limit: Int): SubredditCommon {
        return commonApi.getNewSubreddits(accessToken, limit)
    }

    suspend fun searchSubreddits(accessToken: String, limit: Int, query: String): SubredditCommon {
        return commonApi.searchSubreddits(accessToken, limit, query)
    }
}