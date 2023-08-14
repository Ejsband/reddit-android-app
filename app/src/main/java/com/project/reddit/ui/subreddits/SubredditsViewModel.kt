package com.project.reddit.ui.subreddits

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.reddit.domain.AccessTokenDataUseCase
import com.project.reddit.domain.CommonUseCase
import com.project.reddit.entity.Comment
import com.project.reddit.entity.CommentCommon
import com.project.reddit.entity.CommentCommonChildren
import com.project.reddit.entity.CommentData
import com.project.reddit.entity.CommentDataChildren
import com.project.reddit.entity.CommentItemData
import com.project.reddit.entity.Post
import com.project.reddit.entity.PostCommon
import com.project.reddit.entity.PostCommonChildren
import com.project.reddit.entity.PostItemData
import com.project.reddit.entity.Subreddit
import com.project.reddit.entity.SubredditCommon
import com.project.reddit.entity.SubredditCommonChildren
import com.project.reddit.entity.SubredditData
import com.project.reddit.entity.User
import com.project.reddit.entity.UserActivityComment
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.entity.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubredditsViewModel @Inject constructor(
    private val accessTokenDataUseCase: AccessTokenDataUseCase,
    private val commonUseCase: CommonUseCase
) : ViewModel() {

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

    fun reloadUserCommentState(alias: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            viewModelScope.launch(Dispatchers.IO) {
                val comments = commonUseCase.getUserActivityComments(header, alias)
                if (comments.data.children.isEmpty()) {

                } else {
                    _userCommentState.value = comments
                }
            }
        }
    }

    private val _userInfoState = MutableStateFlow(
        UserData(
            User(
                id = "000000",
                name = "username",
                image = "",
                friendsAmount = 0
            )
        )
    )
    val userInfoState = _userInfoState.asStateFlow()

    fun reloadUserInfoState(alias: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            viewModelScope.launch(Dispatchers.IO) {
                val user = commonUseCase.getUserInfo(header, alias)
                _userInfoState.value = user
            }
        }
    }

    private val _commentState = MutableStateFlow(
        CommentCommon(
            CommentCommonChildren(
                mutableListOf(
                    CommentItemData(
                        Comment(
                            name = "Nothing to show",
                            author = "",
                            creationTime = 0,
                            score = 0,
                            text = ""
                        )
                    )
                )
            )
        )
    )
    val commentState = _commentState.asStateFlow()

    fun reloadCommentsState(link: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            val data = link.split("/")
            val comments = commonUseCase.getTopicComments(header, data[2], data[4], data[5])
            Log.d("XXXXX", comments.size.toString())
            _commentState.value = comments[1]
        }
    }

    fun saveComment(commentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            commonUseCase.saveComment(header, commentId)
        }
    }

    fun unsaveComment(commentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            commonUseCase.unsaveComment(header, commentId)
        }
    }

    fun voteComment(commentId: String, voteDir: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            commonUseCase.voteComment(header, commentId, voteDir)
        }
    }

    private val _subredditInfoState = MutableStateFlow(
        SubredditData(
            Subreddit(
                name = "",
                title = "",
                url = "",
                icon = ""
            )
        )
    )
    val subredditInfoState = _subredditInfoState.asStateFlow()

    fun reloadSubredditInfoState(subredditName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            val subreddit = commonUseCase.getSubredditInfo(header, subredditName)
            _subredditInfoState.value = subreddit
        }
    }

    private val _postState = MutableStateFlow(
        PostCommon(
            PostCommonChildren(
                mutableListOf(
                    PostItemData(
                        Post(
                            title = "Nothing to show",
                            text = "",
                            author = "",
                            link = "",
                            image = ""
                        )
                    )
                )
            )
        )
    )
    val postState = _postState.asStateFlow()

    fun reloadPostState(subredditName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            val posts = commonUseCase.getSubredditTopics(header, subredditName, 100)
            _postState.value = posts
        }
    }

    private val _popularSubredditState = MutableStateFlow(
        SubredditCommon(
            SubredditCommonChildren(
                mutableListOf(
                    SubredditData(
                        Subreddit(
                            name = "",
                            title = "Nothing to show",
                            url = "",
                            icon = ""
                        )
                    )
                )
            )
        )
    )
    val popularSubredditState = _popularSubredditState.asStateFlow()

    fun reloadPopularSubredditState() {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            val subreddits = commonUseCase.getPopularSubreddits(header, 100)
            _popularSubredditState.value = subreddits
        }
    }

    private val _newSubredditState = MutableStateFlow(
        SubredditCommon(
            SubredditCommonChildren(
                mutableListOf(
                    SubredditData(
                        Subreddit(
                            name = "",
                            title = "Nothing to show",
                            url = "",
                            icon = ""
                        )
                    )
                )
            )
        )
    )
    val newSubredditState = _newSubredditState.asStateFlow()

    fun reloadNewSubredditState() {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            val subreddits = commonUseCase.getNewSubreddits(header, 100)
            _newSubredditState.value = subreddits
        }
    }

    private val _searchSubredditState = MutableStateFlow(
        SubredditCommon(
            SubredditCommonChildren(
                mutableListOf(
                    SubredditData(
                        Subreddit(
                            name = "",
                            title = "Nothing to show",
                            url = "",
                            icon = ""
                        )
                    )
                )
            )
        )
    )
    val searchSubredditState = _searchSubredditState.asStateFlow()

    fun reloadSearchSubredditState(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val accessTokenData = accessTokenDataUseCase.getAccessToken("key")
            val header = "${accessTokenData.tokenType} ${accessTokenData.accessToken}"
            val subreddits = commonUseCase.searchSubreddits(header, 100, query)
            _searchSubredditState.value = subreddits
        }
    }

}