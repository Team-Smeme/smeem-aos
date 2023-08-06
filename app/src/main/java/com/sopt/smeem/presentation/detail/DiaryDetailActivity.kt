package com.sopt.smeem.presentation.detail

import android.view.View
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityDiaryDetailBinding
import com.sopt.smeem.presentation.BindingActivity

class DiaryDetailActivity :
    BindingActivity<ActivityDiaryDetailBinding>(R.layout.activity_diary_detail) {

    private val viewModel by viewModels<DiaryDetailViewModel>()

    override fun constructLayout() {
        // databinding
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    override fun addListeners() {
        binding.btnDiaryDetailBack.setOnClickListener {
            finish()
        }
        binding.btnDiaryDetailMenu.setOnClickListener {
            DiaryDetailBottomSheet().show(supportFragmentManager, DiaryDetailBottomSheet.TAG)
        }
    }

    override fun addObservers() {
        viewModel.isTopicExist.observe(this) {
            if (!it) binding.layoutDiaryDetailRandomTopic.layoutSection.visibility = View.GONE
        }
    }
}