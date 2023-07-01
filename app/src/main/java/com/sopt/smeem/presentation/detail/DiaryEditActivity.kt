package com.sopt.smeem.presentation.detail

import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityDiaryEditBinding
import com.sopt.smeem.presentation.BindingActivity

class DiaryEditActivity : BindingActivity<ActivityDiaryEditBinding>(R.layout.activity_diary_edit) {

    override fun constructLayout() {
        binding.etDiaryEditContent.requestFocus()
    }
}