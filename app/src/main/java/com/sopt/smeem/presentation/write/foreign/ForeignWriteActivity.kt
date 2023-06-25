package com.sopt.smeem.presentation.write.foreign

import android.graphics.Color
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityForeignWriteBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.TooltipUtil.createTopicTooltip
import com.sopt.smeem.util.showSnackbar

class ForeignWriteActivity :
    BindingActivity<ActivityForeignWriteBinding>(R.layout.activity_foreign_write) {

    private val viewModel by viewModels<ForeignWriteViewModel>()

    override fun constructLayout() {
        with(binding) {
            // databinding
            vm = viewModel
            lifecycleOwner = this@ForeignWriteActivity
            // ui
            etForeignWrite.requestFocus()
            layoutForeignWriteRandomTopic.layoutSection.visibility = View.GONE
            showTooltip(lifecycleOwner)
        }
    }

    override fun addListeners() {
        setTopicVisibility()
        refreshTopic()
        completeDiary()
    }

    override fun addObservers() {
        checkDiary()
    }

    private fun showTooltip(owner: LifecycleOwner?) {
        // TODO: 최초 실행 여부 조건문
        binding.layoutForeignWriteBottomToolbar.cbRandomTopic.createTopicTooltip(
            this@ForeignWriteActivity, owner
        )
    }

    private fun setTopicVisibility() {
        with(binding) {
            layoutForeignWriteBottomToolbar.cbRandomTopic.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        layoutForeignWriteBottomToolbar.tvRandomTopicLabel.setTextColor(
                            resources.getColor(R.color.point, null)
                        )
                        // TODO: 새로운 랜덤 주제 불러오기
                        layoutForeignWriteRandomTopic.layoutSection.visibility = View.VISIBLE
                    }

                    false -> {
                        layoutForeignWriteBottomToolbar.tvRandomTopicLabel.setTextColor(
                            resources.getColor(R.color.gray_500, null)
                        )
                        layoutForeignWriteRandomTopic.layoutSection.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun refreshTopic() {
        binding.layoutForeignWriteRandomTopic.btnRefresh.setOnClickListener {
            // TODO: 새로운 랜덤 주제 불러오기
        }
    }

    private fun completeDiary() {
        binding.layoutForeignWriteToolbar.tvDone.setOnClickListener {
            when (viewModel.isValidDiary.value) {
                true -> {
                    // TODO: 홈 뷰로 이동
                }
                else -> {
                    binding.root.showSnackbar(
                        "외국어를 포함해 일기를 작성해 주세요 :(",
                        R.id.layout_foreign_write_bottom_toolbar
                    )
                }
            }
        }
    }

    private fun checkDiary() {
        viewModel.isValidDiary.observe(this) {
            when (it) {
                true -> {
                    binding.layoutForeignWriteToolbar.tvDone.setTextColor(
                        resources.getColor(R.color.point, null)
                    )
                }
                false -> {
                    binding.layoutForeignWriteToolbar.tvDone.setTextColor(
                        resources.getColor(R.color.gray_300, null)
                    )
                }
            }
        }
    }

}