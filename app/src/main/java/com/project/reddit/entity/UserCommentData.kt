package com.project.reddit.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_comment_data")
class UserCommentData(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "body")
    val body: String,
    @ColumnInfo(name = "link_title")
    val postTitle: String,
    @ColumnInfo(name = "subreddit")
    val subreddit: String,
)