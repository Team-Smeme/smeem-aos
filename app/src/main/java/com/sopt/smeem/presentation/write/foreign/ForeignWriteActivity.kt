package com.sopt.smeem.presentation.write.foreign

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityForeignWriteBinding
import com.sopt.smeem.description
import com.sopt.smeem.domain.model.RetrievedBadge
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.home.HomeActivity
import com.sopt.smeem.util.TooltipUtil.createTopicTooltip
import com.sopt.smeem.util.setOnSingleClickListener
import com.sopt.smeem.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.toImmutableList
import java.io.Serializable

@AndroidEntryPoint
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
        goBackToHome()
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

    private fun goBackToHome() {
        binding.layoutForeignWriteToolbar.tvCancel.setOnSingleClickListener {
            finish()
        }
    }

    private fun setTopicVisibility() {
        with(binding) {
            layoutForeignWriteBottomToolbar.cbRandomTopic.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        layoutForeignWriteBottomToolbar.tvRandomTopicLabel.setTextColor(
                            resources.getColor(R.color.point, null)
                        )
                        setRandomTopic()
                        layoutForeignWriteRandomTopic.layoutSection.visibility = View.VISIBLE
                    }

                    false -> {
                        layoutForeignWriteBottomToolbar.tvRandomTopicLabel.setTextColor(
                            resources.getColor(R.color.gray_500, null)
                        )
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
            when (viewModel.isValidDiary.value) {
                true -> {
                    viewModel.uploadDiary(
                        onSuccess = {
                            Intent(this, HomeActivity::class.java).apply {
                                putExtra("retrievedBadge", it as Serializable)
                            }.run(::startActivity)
                            finishAffinity()
                        },
                        onError = { e ->
                            Toast.makeText(this, e.description(), Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                else -> {
                    binding.root.showSnackbar(
                        "외국어를 포함해 일기를 작성해 주세요 :(",
                        R.id.layout_foreign_write_bottom_toolbar,
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