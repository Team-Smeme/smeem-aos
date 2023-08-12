package com.sopt.smeem.presentation.write.natiive

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityNativeWriteStep1Binding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.TooltipUtil.createTopicTooltip
import com.sopt.smeem.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    }

    override fun addObservers() {
        checkDiary()
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
                        setRandomTopic()
                        layoutNativeStep1RandomTopic.layoutSection.visibility = View.VISIBLE
                    }

                    false -> {
                        layoutNativeStep1BottomToolbar.tvRandomTopicLabel.setTextColor(
                            resources.getColor(R.color.gray_500, null)
                        )
                        viewModel.topicId = -1
                        layoutNativeStep1RandomTopic.layoutSection.visibility = View.GONE
                        viewModel.topic.value = ""
                    }
                }
            }
        }
    }

    private fun refreshTopic() {
        binding.layoutNativeStep1RandomTopic.btnRefresh.setOnClickListener {
            setRandomTopic()
        }
    }

    private fun setRandomTopic() {
        viewModel.getRandomTopic { e ->
            Toast.makeText(this@NativeWriteStep1Activity, e.description(), Toast.LENGTH_SHORT).show()
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
        translate()

        viewModel.translateResult.observe(this@NativeWriteStep1Activity) {
            val intent = Intent(this, NativeWriteStep2Activity::class.java).apply {
                putExtra("translateResult", it)
                putExtra("nativeDiary", viewModel.diary.value)
                putExtra("topicId", viewModel.topicId)
            }
            startActivity(intent)
        }
    }

    private fun translate() {
        viewModel.translate()
    }
}