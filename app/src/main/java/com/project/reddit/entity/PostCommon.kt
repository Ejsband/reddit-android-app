package com.project.reddit.entity

import com.google.gson.annotations.SerializedName

class PostCommon(
    @SerializedName("data")
    val data: PostCommonChildren
)

class PostCommonChildren(
    @SerializedName("children")
    val children: List<PostItemData>
)

class PostItemData(
    @SerializedName("data")
    val data: Post
)

class Post(
    @SerializedName("title")
    val title: String,
    @SerializedName("selftext")
    val text: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("permalink")
    val link: String,
    @SerializedName("thumbnail")
    val image: String
)