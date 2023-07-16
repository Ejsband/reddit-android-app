package com.project.reddit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.reddit.databinding.FragmentBoardingLoginBinding

class BoardingLoginFragment : Fragment() {

    private var _binding: FragmentBoardingLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBoardingLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pressButton.setOnClickListener {
            openBrowser()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


private fun openBrowser() {
    val intent = Intent(Intent.ACTION_VIEW, composeUrl())
    this.startActivity(intent)
}

private fun composeUrl(): Uri =
    Uri.parse(resources.getString(R.string.parse_url)).buildUpon()
        .appendQueryParameter("client_id", resources.getString(R.string.client_id))
        .appendQueryParameter("response_type", resources.getString(R.string.response_type))
        .appendQueryParameter("state", resources.getString(R.string.state))
        .appendQueryParameter("redirect_uri", resources.getString(R.string.redirect_uri))
        .appendQueryParameter("duration", resources.getString(R.string.duration))
        .appendQueryParameter("scope", resources.getString(R.string.scope))
        .build()
}