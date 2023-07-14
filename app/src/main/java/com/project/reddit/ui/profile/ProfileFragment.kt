package com.project.reddit.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.reddit.MainActivity
import com.project.reddit.R
import com.project.reddit.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

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

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.navbar_logout_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when (menuItem.itemId) {
                    R.id.logout -> {
                        createDialog()
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun createDialog() {
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