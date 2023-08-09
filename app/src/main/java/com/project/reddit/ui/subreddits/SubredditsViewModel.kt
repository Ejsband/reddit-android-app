package com.project.reddit.ui.subreddits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.reddit.domain.AccessTokenDataUseCase
import com.project.reddit.domain.CommonUseCase
import com.project.reddit.entity.Post
import com.project.reddit.entity.PostCommon
import com.project.reddit.entity.PostCommonChildren
import com.project.reddit.entity.PostItemData
import com.project.reddit.entity.Subreddit
import com.project.reddit.entity.SubredditCommon
import com.project.reddit.entity.SubredditCommonChildren
import com.project.reddit.entity.SubredditData
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

    private val _subredditInfoState = MutableStateFlow(
        SubredditData(
            Subreddit(
                name = "Blank",
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
                            author = "ewtyewy",
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