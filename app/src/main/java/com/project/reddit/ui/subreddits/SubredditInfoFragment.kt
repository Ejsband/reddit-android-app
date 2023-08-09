package com.project.reddit.ui.subreddits

import android.content.Intent
import android.graphics.Color
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
import com.bumptech.glide.Glide
import com.project.reddit.R
import com.project.reddit.databinding.FragmentSubredditInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubredditInfoFragment : Fragment() {

    private var _binding: FragmentSubredditInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubredditsViewModel by viewModels()

    private var link: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubredditInfoBinding.inflate(inflater, container, false)
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
                        findNavController().navigate(R.id.action_navigation_subreddit_info_to_navigation_posts, bundle)
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner)

        viewModel.reloadSubredditInfoState(arguments?.getString("subredditName")!!)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subredditInfoState.collect { subreddit ->
                    Glide.with(binding.subredditPhoto).clear(binding.subredditPhoto)
                    Glide.with(binding.subredditPhoto).load(subreddit.data.icon).into(binding.subredditPhoto)
                    binding.name.text = subreddit.data.title
                    link = "https://reddit.com/${subreddit.data.url}"
                }
            }
        }

        binding.buttonFollow.setOnClickListener {
            val text = binding.buttonFollow.text
            if (text == "follow") {
                binding.buttonFollow.setBackgroundColor(Color.parseColor("#eb5528"))
                binding.buttonFollow.text = "unfollow"
            } else {
                binding.buttonFollow.setBackgroundColor(Color.parseColor("#000000"))
                binding.buttonFollow.text = "follow"
            }
        }

        binding.buttonShare.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Unsplash")
                val shareMessage = link?.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "Image"))
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Unable to share the image!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}