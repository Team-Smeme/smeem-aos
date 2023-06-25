package com.sopt.smeem.presentation.mypage

import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityMyBadgesBinding
import com.sopt.smeem.domain.model.BadgeType
import com.sopt.smeem.presentation.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyBadgesActivity : BindingActivity<ActivityMyBadgesBinding>(R.layout.activity_my_badges) {
    private val viewModel by viewModels<BadgeViewModel>()
    private lateinit var welcomeAdaptor: MyBadgeWelcomeAdaptor
    private lateinit var accumulatedAdaptor: MyBadgeAccumulatedAdaptor
    private lateinit var continuedAdaptor: MyBadgeContinuedAdaptor
    private lateinit var extraAdaptor: MyBadgeExtraAdaptor

    override fun constructLayout() {
        setUpBadges()
    }

    override fun addListeners() {
        getBadges()
        onTouchBack()
    }

    private fun setUpBadges() {
        welcomeAdaptor = MyBadgeWelcomeAdaptor()
        binding.rvMyBadgesWelcomeBadge.adapter = welcomeAdaptor

        accumulatedAdaptor = MyBadgeAccumulatedAdaptor()
        binding.rvMyBadgesAccumulatedDiaryCount.adapter = accumulatedAdaptor

        continuedAdaptor = MyBadgeContinuedAdaptor()
        binding.rvMyBadgesContinuedDiaryCount.adapter = continuedAdaptor

        extraAdaptor = MyBadgeExtraAdaptor()
        binding.rvMyBadgesExtras.adapter = extraAdaptor
    }

    private fun getBadges() {
        viewModel.getBadges(
            onError = { t ->
                Toast.makeText(this, t.cause.toString(), Toast.LENGTH_SHORT).show()
            }
        )

        viewModel.badges.observe(this) {
            if (it[BadgeType.WELCOME] == null) {
                binding.tvMyBadgesWelcomeBadge.isInvisible = true
            } else {
                binding.tvMyBadgesWelcomeBadge.isInvisible = false
                welcomeAdaptor.submitList(it[BadgeType.WELCOME])
            }

            if (it[BadgeType.ACCUMULATED] == null) {
                binding.tvMyBadgesAccumulatedDiaryCount.isInvisible = true
            } else {
                binding.tvMyBadgesAccumulatedDiaryCount.isInvisible = false
                accumulatedAdaptor.submitList(it[BadgeType.ACCUMULATED])
            }

            if (it[BadgeType.CONTINUED] == null) {
                binding.tvMyBadgesContinuedDiaryCount.isInvisible = true
            } else {
                binding.tvMyBadgesContinuedDiaryCount.isInvisible = false
                continuedAdaptor.submitList(it[BadgeType.CONTINUED])
            }

            if (it[BadgeType.EXTRA] == null) {
                binding.tvMyBadgesExtras.isInvisible = true
            } else {
                binding.tvMyBadgesExtras.isInvisible = false
                extraAdaptor.submitList(it[BadgeType.EXTRA])
            }

        }
    }

    private fun onTouchBack() {
        binding.imgMyBadgesBack.setOnClickListener {
            /* onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                 override fun handleOnBackPressed() {
                     goBack()
                 }
             })*/
            goBack()
            finish()
        }
    }

    private fun goBack() {
        super.onBackPressed()
    }
}