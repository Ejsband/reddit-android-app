package com.project.reddit.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "access_token_data")
class AccessTokenData(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "access_token")
    val accessToken: String,
    @ColumnInfo(name = "token_type")
    val tokenType: String,
    @ColumnInfo(name = "expires_in")
    val expiration: Int,
    @ColumnInfo(name = "refresh_token")
    val refreshToken: String,
    @ColumnInfo(name = "scope")
    val scope: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: String
)