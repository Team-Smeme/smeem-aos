package com.sopt.smeem.presentation.write.natiive

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import com.sopt.smeem.DefaultSnackBar
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityNativeWriteStep1Binding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.write.Constant.tooltipHasNeverChecked
import com.sopt.smeem.util.TooltipUtil.createTopicTooltip
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NativeWriteStep1Activity :
    BindingActivity<ActivityNativeWriteStep1Binding>(R.layout.activity_native_write_step1) {

    private val viewModel by viewModels<NativeWriteStep1ViewModel>()

    override fun constructLayout() {
        getToolTipNeverCheckedFromLocal()

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
        goBackToHome()
        showHint()
        setTopicVisibility()
        refreshTopic()
        completeNativeDiary()
    }

    override fun addObservers() {
        checkDiary()
    }

    private fun getToolTipNeverCheckedFromLocal() {
        tooltipHasNeverChecked = viewModel.getNeverClickedRandomToolTip()
    }

    private fun showTooltip(owner: LifecycleOwner?) {
        if (tooltipHasNeverChecked) {
            binding.layoutNativeStep1BottomToolbar.cbRandomTopic.createTopicTooltip(
                this@NativeWriteStep1Activity, owner
            )
        }
    }

    private fun goBackToHome() {
        binding.layoutNativeStep1Toolbar.tvCancel.setOnSingleClickListener {
            saveToolTipStatus()
            finish()
        }
    }

    private fun showHint() {
        binding.etNativeStep1Write.addTextChangedListener { watcher ->
            binding.tvNativeStep1Hint.visibility =
                if (watcher.isNullOrEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setTopicVisibility() {
        with(binding) {
            layoutNativeStep1BottomToolbar.cbRandomTopic.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        setRandomTopic()
                        layoutNativeStep1RandomTopic.layoutSection.visibility = View.VISIBLE
                    }

                    false -> {
                        viewModel.topicId = -1
                        layoutNativeStep1RandomTopic.layoutSection.visibility = View.GONE
                        viewModel.topic.value = ""
                    }
                }
            }
        }
    }

    private fun refreshTopic() {
        binding.layoutNativeStep1RandomTopic.btnRefresh.setOnSingleClickListener {
            setRandomTopic()
        }
    }

    private fun setRandomTopic() {
        viewModel.getRandomTopic { e ->
            Toast.makeText(this@NativeWriteStep1Activity, e.description(), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun completeNativeDiary() {
        binding.layoutNativeStep1Toolbar.tvDone.setOnSingleClickListener {
            when (viewModel.isValidDiary.value) {
                true -> {
                    moveToStep2()
                }

                else -> {
                    DefaultSnackBar.makeOnTopOf(
                        binding.root,
                        R.id.layout_native_step1_bottom_toolbar,
                        "일기를 작성해 주세요 :("
                    ).show()
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
        saveToolTipStatus()

        viewModel.translateResult.observe(this@NativeWriteStep1Activity) {
            Intent(this, NativeWriteStep2Activity::class.java).apply {
                putExtra("translateResult", it)
                putExtra("nativeDiary", viewModel.diary.value)
                putExtra("topicId", viewModel.topicId)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }.run(::startActivity)
        }
    }

    private fun translate() {
        viewModel.translate()
    }

    private fun saveToolTipStatus() {
        if (!tooltipHasNeverChecked) {
            viewModel.randomTopicTooltipOff()
        }
    }
}