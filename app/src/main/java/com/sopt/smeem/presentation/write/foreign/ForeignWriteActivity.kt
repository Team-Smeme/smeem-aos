package com.sopt.smeem.presentation.write.foreign

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.sopt.smeem.DefaultSnackBar
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityForeignWriteBinding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.home.HomeActivity
import com.sopt.smeem.presentation.write.Constant.tooltipHasNeverChecked
import com.sopt.smeem.util.TooltipUtil.createTopicTooltip
import com.sopt.smeem.util.hideKeyboard
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

@AndroidEntryPoint
class ForeignWriteActivity :
    BindingActivity<ActivityForeignWriteBinding>(R.layout.activity_foreign_write) {

    private val viewModel by viewModels<ForeignWriteViewModel>()

    override fun constructLayout() {
        getTooltipChecked()
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
        goBackToHome()
        setTopicVisibility()
        refreshTopic()
        completeDiary()
    }

    override fun addObservers() {
        checkDiary()
    }

    private fun showTooltip(owner: LifecycleOwner?) {
        if (tooltipHasNeverChecked) {
            binding.layoutForeignWriteBottomToolbar.cbRandomTopic.createTopicTooltip(
                this@ForeignWriteActivity, owner
            )
        }
    }

    private fun goBackToHome() {
        binding.layoutForeignWriteToolbar.tvCancel.setOnSingleClickListener {
            saveToolTipStatus()
            finish()
        }
    }

    private fun setTopicVisibility() {
        with(binding) {
            layoutForeignWriteBottomToolbar.cbRandomTopic.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        setRandomTopic()
                        layoutForeignWriteRandomTopic.layoutSection.visibility = View.VISIBLE
                    }

                    false -> {
                        viewModel.topicId = -1
                        layoutForeignWriteRandomTopic.layoutSection.visibility = View.GONE
                        viewModel.topic.value = ""
                    }
                }
            }
        }
    }

    private fun refreshTopic() {
        binding.layoutForeignWriteRandomTopic.btnRefresh.setOnSingleClickListener {
            setRandomTopic()
        }
    }

    private fun setRandomTopic() {
        viewModel.getRandomTopic { e ->
            Toast.makeText(this@ForeignWriteActivity, e.description(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun completeDiary() {
        binding.layoutForeignWriteToolbar.tvDone.setOnSingleClickListener {
            saveToolTipStatus()
            when (viewModel.isValidDiary.value) {
                true -> {
                    hideKeyboard(currentFocus ?: View(this))
                    viewModel.uploadDiary(
                        onSuccess = {
                            Intent(this, HomeActivity::class.java).apply {
                                putExtra("retrievedBadge", it as Serializable)
                                putExtra("snackbarText", resources.getString(R.string.diary_write_done_message))
                            }.run(::startActivity)
                            finishAffinity()
                        },
                        onError = { e ->
                            Toast.makeText(this, e.description(), Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                else -> {
                    DefaultSnackBar.makeOnTopOf(
                        binding.root,
                        R.id.layout_foreign_write_bottom_toolbar,
                        "외국어를 포함해 일기를 작성해 주세요 :("
                    ).show()
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

    private fun getTooltipChecked() {
        tooltipHasNeverChecked = viewModel.neverClickedRandomToolTip()
    }

    private fun saveToolTipStatus() {
        if (!tooltipHasNeverChecked) {
            viewModel.randomTopicTooltipOff()
        }
    }
}