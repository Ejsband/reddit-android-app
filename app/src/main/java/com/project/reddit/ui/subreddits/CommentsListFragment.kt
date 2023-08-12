package com.project.reddit.ui.subreddits

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.project.reddit.databinding.FragmentCommentsListBinding
import com.project.reddit.entity.CommentCommon
import com.project.reddit.ui.subreddits.adapter.CommentsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentsListFragment : Fragment(), CommentsAdapter.OnSaveButtonClickListener {

    private var _binding: FragmentCommentsListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SubredditsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val link = arguments?.getString("postLink")!!
        Log.d("XXXXX", link)

        loadComments(link)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadComments(link: String) {

        if (checkConnection()) {
            viewModel.reloadCommentsState(link)
            val data: CommentCommon = viewModel.commentState.value
            val myAdapter = CommentsAdapter(data.data.children, this)
            binding.recycler.adapter = myAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.commentState.collect { comments ->
                        myAdapter.setData(comments.data.children)
                    }
                }
            }
        }
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

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}