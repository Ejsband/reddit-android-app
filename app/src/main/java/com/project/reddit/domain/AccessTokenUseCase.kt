package com.project.reddit.domain

import com.project.reddit.entity.AccessToken
import com.project.reddit.repository.OauthRetrofitRepository
import javax.inject.Inject

class AccessTokenUseCase @Inject constructor(private val authRetrofitRepository: OauthRetrofitRepository) {

    suspend fun getAccessToken(
        credentials: String,
        grantType: String,
        code: String,
        redirectUri: String
    ): AccessToken {
        return authRetrofitRepository.getAccessToken(credentials, grantType, code, redirectUri)
    }
}