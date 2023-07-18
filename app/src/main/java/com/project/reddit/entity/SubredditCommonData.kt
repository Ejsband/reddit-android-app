package com.project.reddit.entity

import com.google.gson.annotations.SerializedName

class SubredditCommon(
    @SerializedName("data")
    val data: SubredditCommonChildren
)

class SubredditCommonChildren(
    @SerializedName("children")
    val children: List<SubredditData>
)

class SubredditData(
    @SerializedName("data")
    val data: Subreddit
)

class Subreddit(
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("icon_img")
    val icon: String,
)