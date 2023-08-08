package com.project.reddit.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.project.reddit.R
import com.project.reddit.databinding.FragmentProfileBinding
import com.project.reddit.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.reloadUserState()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userState.collect { user ->
                    with(binding) {
                        Glide
                            .with(profilePhoto.context)
                            .load(user.image)
                            .into(profilePhoto)
                        name.text = user.name
                    }
                }
            }
        }

        binding.buttonLogout.setOnClickListener {
            createDialog()
        }

        binding.buttonFriends.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_profile_friend)
        }

        binding.buttonClearData.setOnClickListener {
            viewModel.deleteUserData()
            Toast.makeText(
                requireContext(),
                "All data deleted",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Attention")
            .setMessage("Are you sure you want to logout?")
            .setIcon(R.drawable.ic_profile)
            .setPositiveButton("Yes") { dialog, id ->
                viewModel.deleteAccessTokenData()
                dialog.cancel()
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }.setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
            .show()
    }
}