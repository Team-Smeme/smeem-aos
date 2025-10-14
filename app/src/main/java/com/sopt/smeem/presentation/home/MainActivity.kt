package com.sopt.smeem.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityMainBinding
import com.sopt.smeem.domain.dto.RetrievedBadgeDto
import com.sopt.smeem.presentation.IntentConstants
import com.sopt.smeem.presentation.base.DefaultSnackBar
import com.sopt.smeem.presentation.base.showCustomToast
import com.sopt.smeem.presentation.bookmark.BookmarkFragment
import com.sopt.smeem.presentation.splash.SplashLoginActivity
import com.sopt.smeem.util.getParcelableArrayListExtraCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val homeViewModel by viewModels<HomeViewModel>()

    private val homeFragment by lazy { HomeFragment() }
    private val bookmarkFragment by lazy { BookmarkFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setNavigation()
        handleIntentData()
        handleSendIntent()
    }

    private fun initView() {
        if (supportFragmentManager.findFragmentById(R.id.fcv_main) == null) {
            replaceFragment(homeFragment)
        }
    }

    private fun setNavigation() {
        binding.bnvMain.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> replaceFragment(homeFragment)
                R.id.menu_bookmark -> replaceFragment(bookmarkFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fcv_main, fragment)
            setReorderingAllowed(true)
        }
    }

    private fun handleIntentData() {
        intent.getStringExtra(IntentConstants.SNACKBAR_TEXT)?.let {
            DefaultSnackBar.make(binding.root, it).show()
            intent.removeExtra(IntentConstants.SNACKBAR_TEXT)
        }

        intent.getParcelableArrayListExtraCompat<RetrievedBadgeDto>(IntentConstants.RETRIEVED_BADGE_DTO)
            ?.let {
                val badgeList = it.asReversed()
                badgeList.forEach { badge ->
                    BadgeDialogFragment.newInstance(
                        badge.name,
                        badge.imageUrl,
                        homeViewModel.isFirstBadge,
                    ).show(supportFragmentManager, "badgeDialog")

                    if (homeViewModel.isFirstBadge) {
                        homeViewModel.isFirstBadge = false
                    }
                }
                intent.removeExtra(IntentConstants.RETRIEVED_BADGE_DTO)
            }
    }

    fun hideBottomNavigation() {
        binding.bnvMain.visibility = View.GONE
        binding.viewMainBnvBorder.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.bnvMain.visibility = View.VISIBLE
        binding.viewMainBnvBorder.visibility = View.VISIBLE
    }

    fun navigateToHome() {
        replaceFragment(homeFragment)
        binding.bnvMain.selectedItemId = R.id.menu_home
        showBottomNavigation()
    }
    
    private fun handleSendIntent() {
        if (intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain") {
            val sharedUrl = intent?.getStringExtra(Intent.EXTRA_TEXT)
            
            if (sharedUrl != null && sharedUrl.contains("instagram.com")) {
                lifecycleScope.launch {
                    val isAuthenticated = homeViewModel.isAuthenticated()
                    
                    if (isAuthenticated) {
                        navigateToBookmarkFromUrl(sharedUrl)
                    } else {
                        val loginIntent = Intent(this@MainActivity, SplashLoginActivity::class.java)
                        startActivity(loginIntent)
                        showCustomToast(
                            title = "다시 시도해 주세요",
                            subtitle = "로그인을 하고 다시 북마크를 시도해주세요."
                        )
                        finish()
                    }
                }
            } else {
                showCustomToast(
                    title = "아직 지원하지 않는 링크예요",
                    subtitle = "인스타그램을 잠을 바탕으로 할게 지원할 수 있어요."
                )
            }
        }
    }
    
    private fun navigateToBookmarkFromUrl(url: String) {
        replaceFragment(bookmarkFragment)
        binding.bnvMain.selectedItemId = R.id.menu_bookmark
        bookmarkFragment.navigateToUrlDetail(url)
    }
}
