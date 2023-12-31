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
import com.project.reddit.databinding.FragmentCommentsBinding
import com.project.reddit.entity.CommentCommon
import com.project.reddit.ui.subreddits.adapter.CommentsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentsFragment : Fragment(), CommentsAdapter.OnItemClickListener {

    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubredditsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
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
                        val bundle = Bundle()
                        bundle.putString("subredditName", arguments?.getString("subredditName")!!)
                        findNavController().navigate(R.id.action_navigation_comments_to_navigation_posts, bundle)
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner)

        binding.postTitle.text = arguments?.getString("postTitle")!!
        binding.postText.text = arguments?.getString("postText")!!

        val link = arguments?.getString("postLink")!!

        loadComments(link)

        binding.showAllButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("subredditName", arguments?.getString("subredditName")!!)
            bundle.putString("postTitle", arguments?.getString("postTitle")!!)
            bundle.putString("postText", arguments?.getString("postText")!!)
            bundle.putString("postLink", arguments?.getString("postLink")!!)
            findNavController().navigate(R.id.action_navigation_comments_to_navigation_comments_list, bundle)
        }
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
                        if (comments.data.children.size > 5) {
                            myAdapter.setData(comments.data.children.subList(0, 5))
                        } else {
                            myAdapter.setData(comments.data.children)
                        }
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

    override fun onRootViewClick(view: View, position: Int) {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.commentState.collect { comments ->
                val bundle = Bundle()
                bundle.putString("fragment", "comment")
                bundle.putString("userName", comments.data.children[position].data.author)
                bundle.putString("subredditName", arguments?.getString("subredditName")!!)
                bundle.putString("postTitle", arguments?.getString("postTitle")!!)
                bundle.putString("postText", arguments?.getString("postText")!!)
                bundle.putString("postLink", arguments?.getString("postLink")!!)
                findNavController().navigate(R.id.action_navigation_comments_to_navigation_profile_info, bundle)
            }
        }
    }

    override fun onSaveButtonClick(view: View, position: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.commentState.collect { comments ->
                viewModel.saveComment(comments.data.children[position].data.name)
            }
        }
        Toast.makeText(
            requireContext(),
            "Comment was saved",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onMinusButtonClick(view: View, position: Int) {
        view.rootView
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.commentState.collect { comments ->
                viewModel.voteComment(comments.data.children[position].data.name, "-1")
            }
        }
    }

    override fun onPlusButtonClick(view: View, position: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.commentState.collect { comments ->
                viewModel.voteComment(comments.data.children[position].data.name, "1")
            }
        }
    }
}