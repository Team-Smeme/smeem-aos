package com.sopt.smeem.presentation.detail

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.sopt.smeem.DefaultSnackBar
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityDiaryEditBinding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.home.HomeActivity
import com.sopt.smeem.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryEditActivity : BindingActivity<ActivityDiaryEditBinding>(R.layout.activity_diary_edit) {
    private val viewModel by viewModels<DiaryEditViewModel>()

    override fun constructLayout() {
        // databinding
        binding.vm = viewModel
        binding.lifecycleOwner = this
        // ui
        viewModel.diaryId = intent.getLongExtra("diaryId", -1)
        with (binding.etDiaryEditContent) {
            requestFocus()
            viewModel.diary.value = intent.getStringExtra("originalContent")
            postDelayed(
                { setSelection(text.length) },
                50
            )
        }
        checkRandomTopic()
    }

    override fun addListeners() {
        binding.btnDiaryEditCancel.setOnClickListener {
            finish()
        }
        binding.btnDiaryEditDone.setOnClickListener {
            completeDiary()
        }
    }

    override fun addObservers() {
        checkDiary()
    }

    private fun checkRandomTopic() {
        // 랜덤주제가 없으면 창 없애기
        if (intent.getStringExtra("randomTopic") == "") {
            binding.layoutDiaryEditRandomTopic.layoutSection.visibility = View.GONE
        } else {
            binding.layoutDiaryEditRandomTopic.randomTopic = intent.getStringExtra("randomTopic")
        }
    }

    private fun completeDiary() {
        when (viewModel.isValidDiary.value) {
            true -> {
                viewModel.editDiary(
                    onSuccess = {
                        Intent(this, HomeActivity::class.java).apply {
                            putExtra("snackbarText", resources.getString(R.string.diary_edit_done_message))
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }.run(::startActivity)
                    },
                    onError = { e ->
                        Toast.makeText(this, e.description(), Toast.LENGTH_SHORT).show()
                    }
                )
            }
            else -> {
                DefaultSnackBar.make(binding.root, "외국어를 포함해 일기를 작성해 주세요 :(").show()
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