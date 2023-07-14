package com.project.reddit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.reddit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.checkIfAccessTokenDataExists()

        viewModel.accessTokenExists.observe(this) {
            if (it) {
                startActivity(Intent(this, NavigationActivity::class.java))
            }
        }

        binding.pressButton.setOnClickListener {
            openBrowser()
        }
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

    private fun handleDeepLink(intent: Intent) {
//        Log.d("XXXXX", "Your uri is ${intent.data}")
        if (intent.action != Intent.ACTION_VIEW) return
        val deepLinkUrl = intent.data ?: return
        if (deepLinkUrl.queryParameterNames.contains("code")) {
            val authCode = deepLinkUrl.getQueryParameter("code") ?: return
//            Log.d("XXXXX", "Your code is $authCode")
            viewModel.createAccessTokenData(
                resources.getString(R.string.credentials),
                resources.getString(R.string.grant_type),
                authCode,
                resources.getString(R.string.redirect_uri)
            )
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleDeepLink(it) }
    }
}