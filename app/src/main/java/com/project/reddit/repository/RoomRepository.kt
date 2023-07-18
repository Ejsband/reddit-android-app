package com.project.reddit.repository

import com.project.reddit.data.AccessTokenDataDao
import com.project.reddit.data.AppDatabase
import com.project.reddit.data.UserCommentDataDao
import com.project.reddit.data.UserSavedPostDataDao
import com.project.reddit.data.UserSubmittedPostDataDao
import javax.inject.Inject

class RoomRepository @Inject constructor(private val db: AppDatabase) {
    fun getAccessTokenDataDao(): AccessTokenDataDao {
        return db.accessTokenDataDao()
    }

    fun getUserCommentDataDao(): UserCommentDataDao {
        return db.getUserCommentDataDao()
    }

    fun getUserSavedPostDataDao(): UserSavedPostDataDao {
        return db.getUserSavedPostDataDao()
    }

    fun getUserSubmittedPostDataDao(): UserSubmittedPostDataDao {
        return db.getUserSubmittedPostDataDao()
    }
}