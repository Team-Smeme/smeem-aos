package com.sopt.smeem.presentation.write.foreign

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityForeignWriteBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.TooltipUtil.createTopicTooltip

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
//                        layoutForeignWriteRandomTopic.tvContent.text =
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

}