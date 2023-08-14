package com.project.reddit.ui.subreddits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.project.reddit.databinding.FragmentProfileInfoBinding
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.ui.favourite.FavouriteCommentAdapter
import com.project.reddit.ui.favourite.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileInfoFragment : Fragment() {

    private var _binding: FragmentProfileInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadComments()

        binding.buttonFriends.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadComments() {
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
    }
}