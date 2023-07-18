package com.project.reddit.ui.favourite

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.project.reddit.databinding.FragmentFavouriteBinding
import com.project.reddit.entity.CommentData
import com.project.reddit.entity.PostData
import com.project.reddit.entity.UserActivityComment
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.entity.UserActivityPost
import com.project.reddit.entity.UserActivityPostData
import com.project.reddit.entity.UserCommentData
import com.project.reddit.entity.UserSavedPostData
import com.project.reddit.entity.UserSubmittedPostData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPosts()

        binding.buttonSavedComments.setOnClickListener {
            loadSavedPosts()
        }

        binding.buttonSubreddits.setOnClickListener {
            loadPosts()
        }

        binding.buttonComments.setOnClickListener {
            loadComments()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadPosts() {

        binding.buttonSubreddits.setBackgroundColor(Color.parseColor("#eb5528"))
        binding.buttonComments.setBackgroundColor(Color.parseColor("#000000"))
        binding.buttonSavedComments.setBackgroundColor(Color.parseColor("#000000"))

        if (checkConnection()) {
            viewModel.reloadUserPostState()
            val data: UserActivityPostData = viewModel.userPostState.value
            val myAdapter = FavouritePostAdapter(data.data.children) { }
            binding.recycler.adapter = myAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.userPostState.collect { post ->
                        myAdapter.setData(post.data.children)
                        viewModel.saveUserPostData()
                    }
                }
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                val postList = withContext(Dispatchers.IO) {
                    viewModel.postDataUseCase.getUserSubmittedPostData()
                }
                if (postList.isNotEmpty()) {
                    val myAdapter =
                        FavouritePostAdapter(convertUserSubmittedPostDataToPostData(postList)) { }
                    binding.recycler.adapter = myAdapter
                    Toast.makeText(
                        requireContext(),
                        "Cached data loaded",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    binding.recycler.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        "Information not available",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }



    }

    private fun convertUserSubmittedPostDataToPostData(data: List<UserSubmittedPostData>): List<PostData> {
        val list = mutableListOf<PostData>()
        for (item in data) {
            list.add(
                PostData(
                    UserActivityPost(
                        item.name,
                        item.title,
                        item.commentNumber,
                        item.subreddit,
                        item.image
                    )
                )
            )
        }
        return list
    }

    private fun loadSavedPosts() {

        binding.buttonSubreddits.setBackgroundColor(Color.parseColor("#000000"))
        binding.buttonComments.setBackgroundColor(Color.parseColor("#000000"))
        binding.buttonSavedComments.setBackgroundColor(Color.parseColor("#eb5528"))

        if (checkConnection()) {
            viewModel.reloadUserSavedPostState()
            val data: UserActivityPostData = viewModel.userSavedPostState.value
            val myAdapter = FavouritePostAdapter(data.data.children) { }
            binding.recycler.adapter = myAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.userSavedPostState.collect { post ->
                        myAdapter.setData(post.data.children)
                        viewModel.saveUserSavedPostData()
                    }
                }
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                val savedPostList = withContext(Dispatchers.IO) {
                    viewModel.savedPostDataUseCase.getUserSavedPostData()
                }
                if (savedPostList.isNotEmpty()) {
                    val myAdapter =
                        FavouritePostAdapter(convertUserSavedPostDataToPostData(savedPostList)) { }
                    binding.recycler.adapter = myAdapter
                    Toast.makeText(
                        requireContext(),
                        "Cached data loaded",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    binding.recycler.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        "Information not available",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun convertUserSavedPostDataToPostData(data: List<UserSavedPostData>): List<PostData> {
        val list = mutableListOf<PostData>()
        for (item in data) {
            list.add(
                PostData(
                    UserActivityPost(
                        item.name,
                        item.title,
                        item.commentNumber,
                        item.subreddit,
                        item.image
                    )
                )
            )
        }
        return list
    }

    private fun loadComments() {

        binding.buttonSubreddits.setBackgroundColor(Color.parseColor("#000000"))
        binding.buttonComments.setBackgroundColor(Color.parseColor("#eb5528"))
        binding.buttonSavedComments.setBackgroundColor(Color.parseColor("#000000"))

        if (checkConnection()) {
            viewModel.reloadUserCommentState()
            val data: UserActivityCommentData = viewModel.userCommentState.value
            val myAdapter = FavouriteCommentAdapter(data.data.children) { }
            binding.recycler.adapter = myAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.userCommentState.collect { comment ->
                        myAdapter.setData(comment.data.children)
                        viewModel.saveUserCommentData()
                    }
                }
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                val commentList = withContext(Dispatchers.IO) {
                    viewModel.commentDataUseCase.getUserCommentData()
                }
                if (commentList.isNotEmpty()) {
                    val myAdapter =
                        FavouriteCommentAdapter(convertCommentDataToUserActivityComment(commentList)) { }
                    binding.recycler.adapter = myAdapter
                    Toast.makeText(
                        requireContext(),
                        "Cached data loaded",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    binding.recycler.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        "Information not available",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun convertCommentDataToUserActivityComment(data: List<UserCommentData>): List<CommentData> {
        val list = mutableListOf<CommentData>()
        for (item in data) {
            list.add(
                CommentData(
                    UserActivityComment(
                        item.name,
                        item.author,
                        item.body,
                        item.postTitle,
                        item.subreddit
                    )
                )
            )
        }
        return list
    }

    private fun checkConnection(): Boolean {
        val connectionManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectionManager.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        return if (isConnected) {
            true
        } else {
            Toast.makeText(
                requireContext(),
                "No network",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }
}