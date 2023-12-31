package com.project.reddit.ui.subreddits

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
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
import com.project.reddit.databinding.FragmentCommentsListBinding
import com.project.reddit.entity.CommentCommon
import com.project.reddit.ui.subreddits.adapter.CommentsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentsListFragment : Fragment(), CommentsAdapter.OnItemClickListener {

    private var _binding: FragmentCommentsListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SubredditsViewModel by viewModels()

    private var lastPage = 0
    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentsListBinding.inflate(inflater, container, false)
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

                        bundle.putString("postTitle", arguments?.getString("postTitle")!!)
                        bundle.putString("postText", arguments?.getString("postText")!!)
                        bundle.putString("postLink", arguments?.getString("postLink")!!)
                        bundle.putString("subredditName", arguments?.getString("subredditName")!!)
                        findNavController().navigate(
                            R.id.action_navigation_comments_list_to_navigation_comments,
                            bundle
                        )
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner)

        val link = arguments?.getString("postLink")!!

        loadComments(link, 1)

        binding.back.setOnClickListener {
            loadComments(link, binding.page.text.toString().toInt() - 1)
        }

        binding.forward.setOnClickListener {
            loadComments(link, binding.page.text.toString().toInt() + 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun loadComments(link: String, page: Int) {
        if (checkConnection()) {
            viewModel.reloadCommentsState(link)
            val data: CommentCommon = viewModel.commentState.value
            val myAdapter = CommentsAdapter(data.data.children, this)
            binding.recycler.adapter = myAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.commentState.collect { comments ->

                        val chunked = comments.data.children.chunked(5)
                        lastPage = chunked.size

                        if (page < chunked.size) {
                            binding.page.text = (page).toString()
                            binding.back.isEnabled = binding.page.text != "1"
                            binding.forward.isEnabled = true
                            myAdapter.setData(chunked[page - 1])
                            Log.d("XXXXX", "if list size - ${chunked.size}")
                            Log.d("XXXXX", "if page number - ${page}")
                        } else {
                            binding.page.text = (page).toString()
                            binding.forward.isEnabled = false
                            myAdapter.setData(chunked[page - 1])
                            Log.d("XXXXX", "else list size - ${chunked.size}")
                            Log.d("XXXXX", "else page number - ${page}")
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
        val bundle = Bundle()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.commentState.collect { comments ->
                bundle.putString("fragment", "commentList")
                bundle.putString("userName", comments.data.children[position].data.author)
                bundle.putString("subredditName", arguments?.getString("subredditName")!!)
                bundle.putString("postTitle", arguments?.getString("postTitle")!!)
                bundle.putString("postText", arguments?.getString("postText")!!)
                bundle.putString("postLink", arguments?.getString("postLink")!!)
                findNavController().navigate(
                    R.id.action_navigation_comments_list_to_navigation_profile_info,
                    bundle
                )
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