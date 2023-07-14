package com.project.reddit

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
//            Log.d("XXXXXX", "The access token is ${accessToken.accessToken}")

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
                                accessToken.scope
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
                                accessToken.scope
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
                    withContext(Dispatchers.Main) {
                        _accessTokenExists.value = true
                    }
                }
            }
        }
    }
}