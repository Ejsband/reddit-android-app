package com.project.reddit.domain

import com.project.reddit.entity.CommentCommon
import com.project.reddit.entity.PostCommon
import com.project.reddit.entity.SubredditCommon
import com.project.reddit.entity.SubredditData
import com.project.reddit.entity.User
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.entity.UserActivityPostData
import com.project.reddit.repository.CommonRetrofitRepository
import javax.inject.Inject

class CommonUseCase @Inject constructor(private val commonRetrofitRepository: CommonRetrofitRepository) {

    suspend fun getUser(accessToken: String): User {
        return commonRetrofitRepository.getUser(accessToken)
    }

    suspend fun getUserInfo(accessToken: String, userAlias: String): User {
        return commonRetrofitRepository.getUserInfo(accessToken, userAlias)
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

    suspend fun getSubredditInfo(accessToken: String, subreddit: String): SubredditData {
        return commonRetrofitRepository.getSubredditInfo(accessToken, subreddit)
    }

    suspend fun getSubredditTopics(accessToken: String, subreddit: String, limit: Int): PostCommon {
        return commonRetrofitRepository.getSubredditTopics(accessToken, subreddit, limit)
    }

    suspend fun getTopicComments(accessToken: String): List<CommentCommon> {
        return commonRetrofitRepository.getTopicComments(accessToken)
    }

    suspend fun saveComment(accessToken: String, commentId: String) {
        return commonRetrofitRepository.saveComment(accessToken, commentId)
    }

    suspend fun unsaveComment(accessToken: String, commentId: String) {
        return commonRetrofitRepository.unsaveComment(accessToken, commentId)
    }

    suspend fun voteComment(accessToken: String, commentId: String, voteDir: String) {
        return commonRetrofitRepository.voteComment(accessToken, commentId, voteDir)
    }
}