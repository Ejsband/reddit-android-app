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
    @SerializedName("replies")
    val replies: CommentCommon,
    @SerializedName("author")
    val author: String,
    @SerializedName("created_utc")
    val creationTime: Int,
    @SerializedName("body")
    val text: String
)