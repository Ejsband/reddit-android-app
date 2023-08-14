package com.project.reddit.ui.subreddits

import android.content.Context
import android.graphics.Color
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
import androidx.navigation.fragment.findNavController
import com.project.reddit.R
import com.project.reddit.databinding.FragmentSubredditsBinding
import com.project.reddit.ui.subreddits.adapter.SubredditsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubredditsFragment : Fragment(), SubredditsAdapter.OnItemClickListener {

    private var _binding: FragmentSubredditsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubredditsViewModel by viewModels()

    private var name = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubredditsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPopularSubreddits()

        binding.popularButton.setOnClickListener {
            loadPopularSubreddits()
        }

        binding.newButton.setOnClickListener {
            loadNewSubreddits()
        }

        binding.searchButton.setOnClickListener {
            loadSearchSubreddits()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadPopularSubreddits() {

        binding.popularButton.setBackgroundColor(Color.parseColor("#eb5528"))
        binding.newButton.setBackgroundColor(Color.parseColor("#000000"))

        if (checkConnection()) {
            viewModel.reloadPopularSubredditState()
            val data = viewModel.popularSubredditState.value
            val myAdapter = SubredditsAdapter(data.data.children, this)
            binding.recycler.adapter = myAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.popularSubredditState.collect { subreddit ->
                        myAdapter.setData(subreddit.data.children)
                        name = 1
                    }
                }
            }
        }
    }

    private fun loadNewSubreddits() {

        binding.newButton.setBackgroundColor(Color.parseColor("#eb5528"))
        binding.popularButton.setBackgroundColor(Color.parseColor("#000000"))

        if (checkConnection()) {
            viewModel.reloadNewSubredditState()
            val data = viewModel.newSubredditState.value
            val myAdapter = SubredditsAdapter(data.data.children, this)
            binding.recycler.adapter = myAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.newSubredditState.collect { subreddit ->
                        myAdapter.setData(subreddit.data.children)
                        name = 2
                    }
                }
            }
        }
    }

    private fun loadSearchSubreddits() {

        if (checkConnection()) {
            val input = binding.textInputEditText.text
            if (input.toString() == "") {
                Toast.makeText(requireContext(), "No text in input", Toast.LENGTH_LONG).show()
            } else {

                binding.newButton.setBackgroundColor(Color.parseColor("#000000"))
                binding.popularButton.setBackgroundColor(Color.parseColor("#000000"))

                viewModel.reloadSearchSubredditState(input.toString())
                val data = viewModel.searchSubredditState.value
                val myAdapter = SubredditsAdapter(data.data.children, this)
                binding.recycler.adapter = myAdapter

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.searchSubredditState.collect { subreddit ->
                            myAdapter.setData(subreddit.data.children)
                            name = 3
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

    override fun onSubscribeButtonClick(view: View, position: Int) {

    }

    override fun onRootViewClick(view: View, position: Int) {
        when (name) {
            0 -> {}
            1 -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.popularSubredditState.collect { subreddit ->
                        val data = subreddit.data.children[position]
                        val bundle = Bundle()
                        val editedTitle = data.data.url.substring(3, data.data.url.length - 1)
                        bundle.putString("subredditName", editedTitle)
                        findNavController().navigate(
                            R.id.action_navigation_subreddits_to_navigation_posts,
                            bundle
                        )
                    }
                }
            }

            2 -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.newSubredditState.collect { subreddit ->
                        val data = subreddit.data.children[position]
                        val bundle = Bundle()
                        val editedTitle = data.data.url.substring(3, data.data.url.length - 1)
                        bundle.putString("subredditName", editedTitle)
                        findNavController().navigate(
                            R.id.action_navigation_subreddits_to_navigation_posts,
                            bundle
                        )
                    }
                }
            }
            3 -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.searchSubredditState.collect { subreddit ->
                        val data = subreddit.data.children[position]
                        val bundle = Bundle()
                        val editedTitle = data.data.url.substring(3, data.data.url.length - 1)
                        bundle.putString("subredditName", editedTitle)
                        findNavController().navigate(
                            R.id.action_navigation_subreddits_to_navigation_posts,
                            bundle
                        )
                    }
                }
            }
        }
    }
}