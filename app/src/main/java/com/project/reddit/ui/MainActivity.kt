package com.project.reddit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.project.reddit.R
import com.project.reddit.ui.boarding.BoardingViewPagerAdapter
import com.project.reddit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: BoardingViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BoardingViewPagerAdapter(this)
        binding.viewPagerId.adapter = adapter

        viewModel.checkIfAccessTokenDataExists()

        viewModel.accessTokenExists.observe(this) {
            if (it) {
                startActivity(Intent(this, NavigationActivity::class.java))
            }
        }
    }

    private fun handleDeepLink(intent: Intent) {
        if (intent.action != Intent.ACTION_VIEW) return
        val deepLinkUrl = intent.data ?: return
        if (deepLinkUrl.queryParameterNames.contains("code")) {
            val authCode = deepLinkUrl.getQueryParameter("code") ?: return
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