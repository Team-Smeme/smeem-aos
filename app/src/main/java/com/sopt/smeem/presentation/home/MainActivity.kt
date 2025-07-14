package com.sopt.smeem.presentation.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityMainBinding
import com.sopt.smeem.domain.dto.RetrievedBadgeDto
import com.sopt.smeem.presentation.IntentConstants
import com.sopt.smeem.presentation.base.DefaultSnackBar
import com.sopt.smeem.presentation.bookmark.BookmarkFragment
import com.sopt.smeem.util.getParcelableArrayListExtraCompat
import dagger.hilt.android.AndroidEntryPoint

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

        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        initView()
        setNavigation()
        handleIntentData()
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
}
