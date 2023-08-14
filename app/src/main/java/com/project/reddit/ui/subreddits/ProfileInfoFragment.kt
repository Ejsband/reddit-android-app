package com.project.reddit.ui.subreddits

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import com.project.reddit.databinding.FragmentProfileInfoBinding
import com.project.reddit.entity.UserActivityCommentData
import com.project.reddit.ui.favourite.FavouriteCommentAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileInfoFragment : Fragment() {

    private var _binding: FragmentProfileInfoBinding? = null
    private val binding get() = _binding!!

    private  val viewModel: SubredditsViewModel by viewModels()

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
                        findNavController().navigate(R.id.action_navigation_profile_info_to_navigation_comments, bundle)
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner)

        var user = arguments?.getString("userName")!!

        loadUser(user)
        loadComments(user)

        binding.buttonFriends.setOnClickListener {
            if (binding.buttonFriends.text == "friend") {
                binding.buttonFriends.setBackgroundColor(Color.parseColor("#eb5528"))
                binding.buttonFriends.text = "unfriend"
            } else {
                binding.buttonFriends.setBackgroundColor(Color.parseColor("#000000"))
                binding.buttonFriends.text = "friend"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadComments(alias: String) {
        viewModel.reloadUserCommentState(alias)
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

    private fun loadUser(user: String) {
        viewModel.reloadUserInfoState(user)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userInfoState.collect { user ->
                    with(binding) {
                        Glide.with(profilePhoto).clear(profilePhoto)
                        Glide.with(profilePhoto).load(user.data.image).into(profilePhoto)
                        name.text = user.data.name
                    }
                }
            }
        }
    }
}