package com.sopt.smeem.presentation.mypage

import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import com.sopt.smeem.R
import com.sopt.smeem.data.datasource.BadgeList
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
            if (it[BadgeType.EVENT] == null) {
                binding.tvMyBadgesWelcomeBadge.isInvisible = false
                welcomeAdaptor.submitList(BadgeList.event)
            } else {
                binding.tvMyBadgesWelcomeBadge.isInvisible = false
                welcomeAdaptor.submitList(
                    it[BadgeType.EVENT]!! + BadgeList.event.subList(
                        it[BadgeType.EVENT]!!.size,
                        BadgeList.event.size
                    )
                )
            }

            if (it[BadgeType.COUNTING] == null) {
                binding.tvMyBadgesAccumulatedDiaryCount.isInvisible = false
                accumulatedAdaptor.submitList(BadgeList.counting)
            } else {
                binding.tvMyBadgesAccumulatedDiaryCount.isInvisible = false
                accumulatedAdaptor.submitList(
                    it[BadgeType.COUNTING]!! + BadgeList.counting.subList(
                        it[BadgeType.COUNTING]!!.size,
                        BadgeList.counting.size
                    )
                )
            }

            if (it[BadgeType.COMBO] == null) {
                binding.tvMyBadgesContinuedDiaryCount.isInvisible = false
                continuedAdaptor.submitList(BadgeList.combo)
            } else {
                binding.tvMyBadgesContinuedDiaryCount.isInvisible = false
                continuedAdaptor.submitList(
                    it[BadgeType.COMBO]!! + BadgeList.combo.subList(
                        it[BadgeType.COMBO]!!.size,
                        BadgeList.combo.size
                    )
                )
            }

            if (it[BadgeType.EXPLORATION] == null) {
                binding.tvMyBadgesExtras.isInvisible = false
                extraAdaptor.submitList(BadgeList.exploration)
            } else {
                binding.tvMyBadgesExtras.isInvisible = false
                extraAdaptor.submitList(
                    it[BadgeType.EXPLORATION]!! + BadgeList.exploration.subList(
                        it[BadgeType.EXPLORATION]!!.size,
                        BadgeList.exploration.size
                    )
                )
            }

        }
    }

    private fun onTouchBack() {
        binding.btnMyBadgesBack.setOnClickListener {
            finish()
        }
    }
}