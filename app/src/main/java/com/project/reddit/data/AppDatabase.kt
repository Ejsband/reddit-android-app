package com.project.reddit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.reddit.entity.AccessTokenData

@Database(entities = [AccessTokenData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accessTokenDataDao(): AccessTokenDataDao
}