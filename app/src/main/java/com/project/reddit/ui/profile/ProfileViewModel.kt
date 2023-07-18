package com.project.reddit.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.reddit.domain.AccessTokenDataUseCase
import com.project.reddit.domain.CommonUseCase
import com.project.reddit.domain.UserCommentDataUseCase
import com.project.reddit.domain.UserSavedPostDataUseCase
import com.project.reddit.domain.UserSubmittedPostDataUseCase
import com.project.reddit.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel@Inject constructor(
    private val accessTokenDataUseCase: AccessTokenDataUseCase,
    private val commonUseCase: CommonUseCase,
    private val userCommentDataUseCase: UserCommentDataUseCase,
    private val userSavedPostDataUseCase: UserSavedPostDataUseCase,
    private val userSubmittedPostDataUseCase: UserSubmittedPostDataUseCase
    ) : ViewModel() {

    private val _userState = MutableStateFlow(
        User(
            id = "000000",
            name = "username",
            image = "",
            friendsAmount = 0
        )
    )
    val userState = _userState.asStateFlow()

    fun reloadUserState() {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            viewModelScope.launch(Dispatchers.IO) {
                val user = commonUseCase.getUser(header)
                _userState.value = user
            }
        }
    }

    fun deleteAccessTokenData() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = accessTokenDataUseCase.getAccessToken("key")
            accessTokenDataUseCase.deleteAccessToken(data)
        }
    }

    fun deleteUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            userCommentDataUseCase.deleteUserCommentData()
            userSavedPostDataUseCase.deleteUserSavedPostData()
            userSubmittedPostDataUseCase.deleteUserSubmittedPostData()
        }
    }
}