package com.sopt.smeem.presentation.write.natiive

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityNativeWriteStep1Binding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.TooltipUtil.createTopicTooltip
import com.sopt.smeem.util.showSnackbar

class NativeWriteStep1Activity :
    BindingActivity<ActivityNativeWriteStep1Binding>(R.layout.activity_native_write_step1) {

    private val viewModel by viewModels<NativeWriteStep1ViewModel>()

    override fun constructLayout() {
        with(binding) {
            // databinding
            vm = viewModel
            lifecycleOwner = this@NativeWriteStep1Activity
            // ui
            etNativeStep1Write.requestFocus()
            layoutNativeStep1RandomTopic.layoutSection.visibility = View.GONE
            showTooltip(lifecycleOwner)
        }
    }

    override fun addListeners() {
        setTopicVisibility()
        refreshTopic()
        hideTip()
        completeNativeDiary()
        goBack()
    }

    override fun addObservers() {
        checkDiary()
    }

    private fun goBack() {
        val toolbar = findViewById<ConstraintLayout>(R.id.layout_native_step1_toolbar)
        val back = toolbar.findViewById<TextView>(R.id.tv_cancel)

        back.setOnClickListener {
            super.onBackPressed()
            finish()
        }
    }

    private fun showTooltip(owner: LifecycleOwner?) {
        // TODO: 최초 실행 여부 조건문
        binding.layoutNativeStep1BottomToolbar.cbRandomTopic.createTopicTooltip(
            this@NativeWriteStep1Activity, owner
        )
    }

    private fun setTopicVisibility() {
        with(binding) {
            layoutNativeStep1BottomToolbar.cbRandomTopic.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        layoutNativeStep1BottomToolbar.tvRandomTopicLabel.setTextColor(
                            resources.getColor(R.color.point, null)
                        )
                        // TODO: 새로운 랜덤 주제 불러오기
                        layoutNativeStep1RandomTopic.layoutSection.visibility = View.VISIBLE
                    }

                    false -> {
                        layoutNativeStep1BottomToolbar.tvRandomTopicLabel.setTextColor(
                            resources.getColor(R.color.gray_500, null)
                        )
                        layoutNativeStep1RandomTopic.layoutSection.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun refreshTopic() {
        binding.layoutNativeStep1RandomTopic.btnRefresh.setOnClickListener {
            // TODO: 새로운 랜덤 주제 불러오기
        }
    }

    private fun hideTip() {
        binding.layoutNativeStep1Tip.setOnClickListener {
            it.visibility = View.GONE
        }
    }

    private fun completeNativeDiary() {
        binding.layoutNativeStep1Toolbar.tvDone.setOnClickListener {
            when (viewModel.isValidDiary.value) {
                true -> {
                    moveToStep2()
                }
                else -> {
                    binding.root.showSnackbar(
                        "외국어를 포함해 일기를 작성해 주세요 :(",
                        R.id.layout_native_step1_bottom_toolbar,
                        Snackbar.LENGTH_SHORT
                    )
                }
            }
        }
    }

    private fun checkDiary() {
        viewModel.isValidDiary.observe(this) {
            when (it) {
                true -> {
                    binding.layoutNativeStep1Toolbar.tvDone.setTextColor(
                        resources.getColor(R.color.point, null)
                    )
                }

                false -> {
                    binding.layoutNativeStep1Toolbar.tvDone.setTextColor(
                        resources.getColor(R.color.gray_300, null)
                    )
                }
            }
        }
    }

    private fun moveToStep2() {
        val intent = Intent(this, NativeWriteStep2Activity::class.java).apply {
            // TODO: 번역 로직 구현
            putExtra("nativeDiary", viewModel.diary.value)
            putExtra("isTopicEnabled", binding.layoutNativeStep1BottomToolbar.cbRandomTopic.isChecked)
        }
        startActivity(intent)
    }

}