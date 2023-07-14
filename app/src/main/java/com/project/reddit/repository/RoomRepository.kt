package com.project.reddit.repository

import com.project.reddit.data.AccessTokenDataDao
import com.project.reddit.data.AppDatabase
import javax.inject.Inject

class RoomRepository @Inject constructor(private val db: AppDatabase) {
    fun getAccessTokenDataDao(): AccessTokenDataDao {
        return db.accessTokenDataDao()
    }
}