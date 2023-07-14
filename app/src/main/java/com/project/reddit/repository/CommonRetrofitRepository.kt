package com.project.reddit.repository

import com.project.reddit.api.CommonApi
import com.project.reddit.entity.User
import javax.inject.Inject

class CommonRetrofitRepository @Inject constructor() {

    private val commonApi = CommonApi.create()

    suspend fun getUser(accessToken: String): User {
        return commonApi.getUser(accessToken)
    }
}