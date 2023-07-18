package com.project.reddit.domain

import com.project.reddit.entity.SubredditCommon
import com.project.reddit.entity.User
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.entity.UserActivityPostData
import com.project.reddit.repository.CommonRetrofitRepository
import javax.inject.Inject

class CommonUseCase @Inject constructor(private val commonRetrofitRepository: CommonRetrofitRepository) {

    suspend fun getUser(accessToken: String): User {
        return commonRetrofitRepository.getUser(accessToken)
    }

    suspend fun getUserActivityPosts(accessToken: String, userAlias: String): UserActivityPostData {
        return commonRetrofitRepository.getUserActivityPosts(accessToken, userAlias)
    }

    suspend fun getUserActivitySavedPosts(accessToken: String, userAlias: String): UserActivityPostData {
        return commonRetrofitRepository.getUserActivitySavedPosts(accessToken, userAlias)
    }

    suspend fun getUserActivityComments(accessToken: String, userAlias: String): UserActivityCommentData {
        return commonRetrofitRepository.getUserActivityComments(accessToken, userAlias)
    }

    suspend fun getPopularSubreddits(accessToken: String, limit: Int): SubredditCommon {
        return commonRetrofitRepository.getPopularSubreddits(accessToken, limit)
    }

    suspend fun getNewSubreddits(accessToken: String, limit: Int): SubredditCommon {
        return commonRetrofitRepository.getNewSubreddits(accessToken, limit)
    }

    suspend fun searchSubreddits(accessToken: String, limit: Int, query: String): SubredditCommon {
        return commonRetrofitRepository.searchSubreddits(accessToken, limit, query)
    }
}