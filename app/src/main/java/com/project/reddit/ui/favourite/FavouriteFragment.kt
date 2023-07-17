package com.project.reddit.ui.favourite

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.project.reddit.databinding.FragmentFavouriteBinding
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.entity.UserActivityPostData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        viewModel.reloadUserPostState()
        binding.buttonSubreddits.setBackgroundColor(Color.parseColor("#eb5528"))
        binding.buttonComments.setBackgroundColor(Color.parseColor("#000000"))
        binding.buttonSavedComments.setBackgroundColor(Color.parseColor("#000000"))
        val data: UserActivityPostData = viewModel.userPostState.value
        val myAdapter = FavouritePostAdapter(data.data.children) { }
        binding.recycler.adapter = myAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userPostState.collect { post ->
                    myAdapter.setData(post.data.children)
                }
            }
        }
    }

    private fun loadSavedPosts() {
        viewModel.reloadUserSavedPostState()
        binding.buttonSubreddits.setBackgroundColor(Color.parseColor("#000000"))
        binding.buttonComments.setBackgroundColor(Color.parseColor("#000000"))
        binding.buttonSavedComments.setBackgroundColor(Color.parseColor("#eb5528"))
        val data: UserActivityPostData = viewModel.userSavedPostState.value
        val myAdapter = FavouritePostAdapter(data.data.children) { }
        binding.recycler.adapter = myAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userSavedPostState.collect { post ->
                    myAdapter.setData(post.data.children)
                }
            }
        }
    }

    private fun loadComments() {
        viewModel.reloadUserCommentState()
        binding.buttonSubreddits.setBackgroundColor(Color.parseColor("#000000"))
        binding.buttonComments.setBackgroundColor(Color.parseColor("#eb5528"))
        binding.buttonSavedComments.setBackgroundColor(Color.parseColor("#000000"))
        val data: UserActivityCommentData = viewModel.userCommentState.value
        val myAdapter = FavouriteCommentAdapter(data.data.children) { }
        binding.recycler.adapter = myAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userCommentState.collect { comment ->
                    myAdapter.setData(comment.data.children)
                }
            }
        }
    }
}