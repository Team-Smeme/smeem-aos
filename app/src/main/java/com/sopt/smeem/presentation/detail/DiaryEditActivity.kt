package com.sopt.smeem.presentation.detail

import android.view.View
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityDiaryEditBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.showSnackbar

class DiaryEditActivity : BindingActivity<ActivityDiaryEditBinding>(R.layout.activity_diary_edit) {
    private val viewModel by viewModels<DiaryEditViewModel>()

    override fun constructLayout() {
        // databinding
        binding.vm = viewModel
        binding.lifecycleOwner = this
        // ui
        with (binding.etDiaryEditContent) {
            requestFocus()
            viewModel.diary.value = intent.getStringExtra("originalContent")
            postDelayed(
                { setSelection(text.length) },
                50
            )
        }
        // 랜덤주제가 없으면 창 없애기
        if (intent.getStringExtra("randomTopic") == "") {
            binding.layoutDiaryEditRandomTopic.layoutSection.visibility = View.GONE
        } else {
            binding.layoutDiaryEditRandomTopic.randomTopic = intent.getStringExtra("randomTopic")
        }
    }

    override fun addListeners() {
        binding.btnDiaryEditCancel.setOnClickListener {
            finish()
        }
        completeDiary()
    }

    override fun addObservers() {
        checkDiary()
    }

    private fun completeDiary() {
        binding.btnDiaryEditDone.setOnClickListener {
            when (viewModel.isValidDiary.value) {
                true -> {
                    // TODO: HomeActivity로 이동
                }
                else -> {
                    binding.root.showSnackbar("외국어를 포함해 일기를 작성해 주세요 :(")
                }
            }
        }
    }

    private fun checkDiary() {
        viewModel.isValidDiary.observe(this) {
            when (it) {
                true -> {
                    binding.btnDiaryEditDone.setTextColor(
                        resources.getColor(R.color.point, null)
                    )
                }
                false -> {
                    binding.btnDiaryEditDone.setTextColor(
                        resources.getColor(R.color.gray_300, null)
                    )
                }
            }
        }
    }
}