package com.project.reddit.api

import com.project.reddit.entity.AccessToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

const val OAUTH_URL = "https://www.reddit.com"

interface OauthApi {

    @POST("/api/v1/access_token")
    suspend fun getAccessToken(
        @Header("Authorization") credentials: String,
        @Query("grant_type") grantType: String,
        @Query("code") code: String,
        @Query("redirect_uri") redirectUri: String
    ): AccessToken

    companion object {
        fun create(): OauthApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(OAUTH_URL)
                .build()
            return retrofit.create(OauthApi::class.java)
        }
    }
}

