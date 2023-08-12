package com.project.reddit.entity

import com.google.gson.annotations.SerializedName

class CommentCommon(
    @SerializedName("data")
    val data: CommentCommonChildren
)

class CommentCommonChildren(
    @SerializedName("children")
    val children: List<CommentItemData>
)

class CommentItemData(
    @SerializedName("data")
    val data: Comment
)

class Comment(
    @SerializedName("name")
    val name: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("created_utc")
    val creationTime: Int,
    @SerializedName("score")
    val score: Int,
    @SerializedName("body")
    val text: String
)