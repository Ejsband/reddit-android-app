package com.project.reddit.ui.profile

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.project.reddit.R
import com.project.reddit.databinding.FragmentProfileFriendBinding
import com.project.reddit.entity.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFriendFragment : Fragment() {

    private var _binding: FragmentProfileFriendBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when (menuItem.itemId) {
                    android.R.id.home -> {
                        findNavController().navigate(R.id.action_navigation_profile_friend_to_navigation_profile)
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner)

        _binding = FragmentProfileFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data: List<User> = listOf(
            User("0000", "alex", "https://sun9-21.userapi.com/impg/c857220/v857220015/196ff1/vNXiBYCv03g.jpg?size=1200x800&quality=96&sign=9ba3015caf3508446e6e75b0984e04fd&type=album", 5),
            User("0000", "boris", "https://i.pinimg.com/originals/9a/cd/1a/9acd1a0fd583bb36f48779a4a35879d8.jpg", 5),
            User("0000", "clara", "https://i.pinimg.com/736x/08/51/3b/08513bc93c885d9f92dff8f05a076a67.jpg", 5),
        )
        val myAdapter = ProfileFriendAdapter(data)

        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.recycler.adapter = myAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}