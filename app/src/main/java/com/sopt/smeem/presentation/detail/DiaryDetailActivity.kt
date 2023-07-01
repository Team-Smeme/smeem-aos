package com.sopt.smeem.presentation.detail

import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityDiaryDetailBinding
import com.sopt.smeem.presentation.BindingActivity

class DiaryDetailActivity :
    BindingActivity<ActivityDiaryDetailBinding>(R.layout.activity_diary_detail) {
    override fun addListeners() {
        binding.btnDiaryDetailBack.setOnClickListener {
            finish()
        }
        binding.btnDiaryDetailMenu.setOnClickListener {
            DiaryDetailBottomSheet().show(supportFragmentManager, DiaryDetailBottomSheet.TAG)
        }
    }
}