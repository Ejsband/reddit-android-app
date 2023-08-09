package com.project.reddit.ui.subreddits

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.project.reddit.R
import com.project.reddit.databinding.FragmentPostsBinding
import com.project.reddit.entity.PostCommon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : Fragment() {

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubredditsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when (menuItem.itemId) {
                    android.R.id.home -> {
                        findNavController().navigate(R.id.action_navigation_posts_to_navigation_subreddits)
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner)

        val subreddit = arguments?.getString("subredditName")!!

        binding.postName.text = subreddit

        loadPosts(subreddit)

        binding.postButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("subredditName", subreddit)
            findNavController().navigate(R.id.action_navigation_posts_to_navigation_subreddit_info, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadPosts(subredditName: String) {

        if (checkConnection()) {
            viewModel.reloadPostState(subredditName)
            val data: PostCommon = viewModel.postState.value
            val myAdapter = PostsAdapter(data.data.children) { }
            binding.recycler.adapter = myAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.postState.collect { post ->
                        myAdapter.setData(post.data.children)
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
}