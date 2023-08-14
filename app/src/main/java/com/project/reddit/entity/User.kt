package com.project.reddit.entity

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("snoovatar_img")
    val image: String,
    @SerializedName("num_friends")
    val friendsAmount: Int
)

class UserData(
    @SerializedName("data")
    val data: User
)