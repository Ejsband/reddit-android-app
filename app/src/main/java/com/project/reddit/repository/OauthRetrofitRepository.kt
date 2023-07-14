package com.project.reddit.repository

import com.project.reddit.api.OauthApi
import com.project.reddit.entity.AccessToken
import javax.inject.Inject

class OauthRetrofitRepository  @Inject constructor() {

    private val oauthApi = OauthApi.create()

    suspend fun getAccessToken(
        credentials: String,
        grantType: String,
        code: String,
        redirectUri: String
    ): AccessToken {
        return oauthApi.getAccessToken(credentials, grantType, code, redirectUri)
    }
}