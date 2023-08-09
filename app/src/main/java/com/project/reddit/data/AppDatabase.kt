package com.project.reddit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.reddit.entity.AccessTokenData
import com.project.reddit.entity.UserCommentData
import com.project.reddit.entity.UserSavedPostData
import com.project.reddit.entity.UserSubmittedPostData

@Database(
    entities = [AccessTokenData::class, UserSavedPostData::class, UserSubmittedPostData::class, UserCommentData::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accessTokenDataDao(): AccessTokenDataDao
    abstract fun getUserCommentDataDao(): UserCommentDataDao
    abstract fun getUserSavedPostDataDao(): UserSavedPostDataDao
    abstract fun getUserSubmittedPostDataDao(): UserSubmittedPostDataDao
}