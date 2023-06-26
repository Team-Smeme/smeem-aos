package com.sopt.smeem.presentation.write.natiive

import android.text.method.ScrollingMovementMethod
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.skydoves.balloon.balloon
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityNativeWriteStep2Binding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.showSnackbar


class NativeWriteStep2Activity :
    BindingActivity<ActivityNativeWriteStep2Binding>(R.layout.activity_native_write_step2) {

    private val viewModel by viewModels<NativeWriteStep2ViewModel>()

    override fun constructLayout() {
        with (binding) {
            // databinding
            vm = viewModel
            lifecycleOwner = this@NativeWriteStep2Activity
            // ui
            etNativeStep1Write.requestFocus()
            with (tvNativeStep2NativeDiary) {
                movementMethod = ScrollingMovementMethod()
                text = intent.getStringExtra("nativeDiary")
            }
            with (layoutNativeStep2BottomToolbar.cbRandomTopic) {
                isChecked = intent.getBooleanExtra("isTopicEnabled", true)
                isEnabled = false
            }
        }
    }

    override fun addListeners() {
        backToStep1()
        toggleHint()
        completeDiary()
    }

    override fun addObservers() {
        checkDiary()
    }

    private fun backToStep1() {
        binding.layoutNativeStep2Toolbar.tvCancel.setOnClickListener {
            finish()
        }
    }

    // TODO: 번역 토글
    private fun toggleHint() {
        binding.layoutNativeStep2BottomToolbar.btnTranslate.setOnClickListener {
            when (binding.layoutNativeStep2BottomToolbar.btnTranslate.isChecked) {
                true -> {

                }
                else -> {

                }
            }
        }
    }

    private fun completeDiary() {
        binding.layoutNativeStep2Toolbar.tvDone.setOnClickListener {
            when (viewModel.isValidDiary.value) {
                true -> {
                    // TODO: 홈 뷰로 이동
                }
                else -> {
                    binding.root.showSnackbar(
                        "외국어를 포함해 일기를 작성해 주세요 :(",
                        R.id.layout_native_step2_bottom_toolbar,
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
                    binding.layoutNativeStep2Toolbar.tvDone.setTextColor(
                        resources.getColor(R.color.point, null)
                    )
                }

                false -> {
                    binding.layoutNativeStep2Toolbar.tvDone.setTextColor(
                        resources.getColor(R.color.gray_300, null)
                    )
                }
            }
        }
    }
}