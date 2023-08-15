package com.project.reddit.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.reddit.domain.AccessTokenUseCase
import com.project.reddit.domain.AccessTokenDataUseCase
import com.project.reddit.entity.AccessTokenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accessTokenUseCase: AccessTokenUseCase,
    private val accessTokenDataUseCase: AccessTokenDataUseCase
) : ViewModel() {

    private val _accessTokenExists = MutableLiveData(false)
    val accessTokenExists = _accessTokenExists

    fun createAccessTokenData(
        credentials: String,
        grantType: String,
        code: String,
        redirectUri: String
    ) {
        viewModelScope.launch {
            val accessToken =
                accessTokenUseCase.getAccessToken(
                    credentials,
                    grantType,
                    code,
                    redirectUri
                )

            withContext(Dispatchers.IO) {
                val accessTokenData =  accessTokenDataUseCase.getAccessToken("key")

                if (accessTokenData == null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        accessTokenDataUseCase.saveAccessToken(
                            AccessTokenData(
                                "key",
                                accessToken.accessToken,
                                accessToken.tokenType,
                                accessToken.expiration,
                                accessToken.refreshToken,
                                accessToken.scope,
                                System.currentTimeMillis().toString()
                            )
                        )
                    }
                    withContext(Dispatchers.Main) {
                        _accessTokenExists.value = true
                    }
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        accessTokenDataUseCase.updateAccessToken(
                            AccessTokenData(
                                "key",
                                accessToken.accessToken,
                                accessToken.tokenType,
                                accessToken.expiration,
                                accessToken.refreshToken,
                                accessToken.scope,
                                System.currentTimeMillis().toString()
                            )
                        )
                    }
                    withContext(Dispatchers.Main) {
                        _accessTokenExists.value = true
                    }
                }
            }

        }
    }

    fun checkIfAccessTokenDataExists() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
                if (accessTokenData == null) {
                    withContext(Dispatchers.Main) {
                        _accessTokenExists.value = false
                    }
                } else {
                    if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - accessTokenData.timestamp.toLong()) < 1400 ) {
                        withContext(Dispatchers.Main) {
                            _accessTokenExists.value = true
                        }
                    } else {
                        deleteAccessTokenData()
                        withContext(Dispatchers.Main) {
                            _accessTokenExists.value = false
                        }
                    }
                }
            }
        }
    }

    private fun deleteAccessTokenData() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = accessTokenDataUseCase.getAccessToken("key")
            accessTokenDataUseCase.deleteAccessToken(data)
        }
    }
}