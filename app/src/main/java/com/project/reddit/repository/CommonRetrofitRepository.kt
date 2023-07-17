package com.project.reddit.repository

import com.project.reddit.api.CommonApi
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
}