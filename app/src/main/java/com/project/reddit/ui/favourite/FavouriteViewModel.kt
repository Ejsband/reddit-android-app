package com.project.reddit.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.reddit.domain.AccessTokenDataUseCase
import com.project.reddit.domain.CommonUseCase
import com.project.reddit.entity.CommentData
import com.project.reddit.entity.CommentDataChildren
import com.project.reddit.entity.PostData
import com.project.reddit.entity.PostDataChildren
import com.project.reddit.entity.UserActivityComment
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.entity.UserActivityPost
import com.project.reddit.entity.UserActivityPostData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val accessTokenDataUseCase: AccessTokenDataUseCase,
    private val commonUseCase: CommonUseCase
) : ViewModel() {

    private val _userPostState = MutableStateFlow(
        UserActivityPostData(
            PostDataChildren(
                mutableListOf(PostData(
                        UserActivityPost(
                            name = "Nothing to show",
                            title = "",
                            commentNumber = "",
                            subreddit = "",
                            image = ""
                        )
                    )
                )
            )
        )
    )
    val userPostState = _userPostState.asStateFlow()

    fun reloadUserPostState() {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            viewModelScope.launch(Dispatchers.IO) {
                val user = commonUseCase.getUser(header)
                val posts = commonUseCase.getUserActivityPosts(header, user.name)

                if (posts.data.children.isEmpty()) {

                } else {
                    _userPostState.value = posts
                }
            }
        }
    }

    private val _userSavedPostState = MutableStateFlow(
        UserActivityPostData(
            PostDataChildren(
                mutableListOf(PostData(
                    UserActivityPost(
                        name = "Nothing to show",
                        title = "",
                        commentNumber = "",
                        subreddit = "",
                        image = ""
                    )
                )
                )
            )
        )
    )
    val userSavedPostState = _userSavedPostState.asStateFlow()

    fun reloadUserSavedPostState() {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            viewModelScope.launch(Dispatchers.IO) {
                val user = commonUseCase.getUser(header)
                val posts = commonUseCase.getUserActivitySavedPosts(header, user.name)

                if (posts.data.children.isEmpty()) {

                } else {
                    _userSavedPostState.value = posts
                }
            }
        }
    }

    private val _userCommentState = MutableStateFlow(
        UserActivityCommentData(
            CommentDataChildren(
                mutableListOf(
                    CommentData(
                    UserActivityComment(
                        name = "Nothing to show",
                        author = "",
                        body = "",
                        postTitle = "",
                        subreddit = ""
                    )
                )
                )
            )
        )
    )
    val userCommentState = _userCommentState.asStateFlow()

    fun reloadUserCommentState() {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            viewModelScope.launch(Dispatchers.IO) {
                val user = commonUseCase.getUser(header)
                val comments = commonUseCase.getUserActivityComments(header, user.name)

                if (comments.data.children.isEmpty()) {

                } else {
                    _userCommentState.value = comments
                }
            }
        }
    }
}