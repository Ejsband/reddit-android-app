package com.project.reddit.entity

import com.google.gson.annotations.SerializedName

class UserActivityPostData(
    @SerializedName("data")
    val data: PostDataChildren
)

class PostDataChildren(
    @SerializedName("children")
    val children: List<PostData>
)

class PostData(
    @SerializedName("data")
    val info: UserActivityPost
)

class UserActivityPost(
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("num_comments")
    val commentNumber: String,
    @SerializedName("subreddit")
    val subreddit: String,
    @SerializedName("thumbnail")
    val image: String
)

//Comment

class UserActivityCommentData(
    @SerializedName("data")
    val data: CommentDataChildren
)

class CommentDataChildren(
    @SerializedName("children")
    val children: List<CommentData>
)

class CommentData(
    @SerializedName("data")
    val info: UserActivityComment
)

class UserActivityComment(
    @SerializedName("name")
    val name: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("link_title")
    val postTitle: String,
    @SerializedName("subreddit")
    val subreddit: String
)