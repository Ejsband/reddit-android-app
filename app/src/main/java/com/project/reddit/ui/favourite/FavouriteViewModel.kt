package com.project.reddit.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.reddit.domain.AccessTokenDataUseCase
import com.project.reddit.domain.CommonUseCase
import com.project.reddit.domain.UserCommentDataUseCase
import com.project.reddit.domain.UserSavedPostDataUseCase
import com.project.reddit.domain.UserSubmittedPostDataUseCase
import com.project.reddit.entity.CommentData
import com.project.reddit.entity.CommentDataChildren
import com.project.reddit.entity.PostData
import com.project.reddit.entity.PostDataChildren
import com.project.reddit.entity.UserActivityComment
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.entity.UserActivityPost
import com.project.reddit.entity.UserActivityPostData
import com.project.reddit.entity.UserCommentData
import com.project.reddit.entity.UserSavedPostData
import com.project.reddit.entity.UserSubmittedPostData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val accessTokenDataUseCase: AccessTokenDataUseCase,
    private val commonUseCase: CommonUseCase,
    private val userCommentDataUseCase: UserCommentDataUseCase,
    private val userSavedPostDataUseCase: UserSavedPostDataUseCase,
    private val userSubmittedPostDataUseCase: UserSubmittedPostDataUseCase
) : ViewModel() {

    val commentDataUseCase = userCommentDataUseCase
    val postDataUseCase = userSubmittedPostDataUseCase
    val savedPostDataUseCase = userSavedPostDataUseCase

    private val _userPostState = MutableStateFlow(
        UserActivityPostData(
            PostDataChildren(
                mutableListOf(
                    PostData(
                        UserActivityPost(
                            name = "",
                            title = "Nothing to show",
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
                val posts = commonUseCase.getUserActivityPosts(header, "Mipedian_Speed")
//                val posts = commonUseCase.getUserActivityPosts(header, user.name)

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
                mutableListOf(
                    PostData(
                        UserActivityPost(
                            name = "",
                            title = "Nothing to show",
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
                            name = "",
                            author = "",
                            body = "Nothing to show",
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

    fun saveUserCommentData() {
        viewModelScope.launch(Dispatchers.IO) {
            val commentList = withContext(Dispatchers.IO) {
                userCommentDataUseCase.getUserCommentData()
            }

            if (commentList.isEmpty()) {
                userCommentState.collect { data ->
                    for (item in data.data.children) {
                        userCommentDataUseCase.saveUserCommentData(
                            UserCommentData(
                                item.info.name,
                                item.info.author,
                                item.info.body,
                                item.info.postTitle,
                                item.info.subreddit
                            )
                        )
                    }
                }

            } else {
                userCommentDataUseCase.deleteUserCommentData()
            }
        }
    }

    fun saveUserPostData() {
        viewModelScope.launch(Dispatchers.IO) {
            val postList = withContext(Dispatchers.IO) {
                userSubmittedPostDataUseCase.getUserSubmittedPostData()
            }

            if (postList.isEmpty()) {
                userPostState.collect { data ->
                    for (item in data.data.children) {
                        userSubmittedPostDataUseCase.saveUserSubmittedPostData(
                            UserSubmittedPostData(
                                item.info.name,
                                item.info.title,
                                item.info.commentNumber,
                                item.info.subreddit,
                                item.info.image
                            )
                        )
                    }
                }

            } else {
                userSubmittedPostDataUseCase.deleteUserSubmittedPostData()
            }
        }
    }

    fun saveUserSavedPostData() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedPostList = withContext(Dispatchers.IO) {
                userSavedPostDataUseCase.getUserSavedPostData()
            }

            if (savedPostList.isEmpty()) {
                userSavedPostState.collect { data ->
                    for (item in data.data.children) {
                        userSavedPostDataUseCase.saveUserSavedPostData(
                            UserSavedPostData(
                                item.info.name,
                                item.info.title,
                                item.info.commentNumber,
                                item.info.subreddit,
                                item.info.image
                            )
                        )
                    }
                }
            } else {
                userSavedPostDataUseCase.deleteUserSavedPostData()
            }
        }
    }
}