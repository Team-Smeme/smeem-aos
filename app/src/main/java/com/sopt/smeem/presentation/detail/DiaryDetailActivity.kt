package com.sopt.smeem.presentation.detail

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityDiaryDetailBinding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.home.HomeActivity
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryDetailActivity :
    BindingActivity<ActivityDiaryDetailBinding>(R.layout.activity_diary_detail) {

    private val viewModel by viewModels<DiaryDetailViewModel>()

    override fun constructLayout() {
        // databinding
        binding.vm = viewModel
        binding.lifecycleOwner = this
        // data
        viewModel.diaryId = intent.getLongExtra("diaryId", -1)
        viewModel.getDiaryDetail { e->
            Toast.makeText(this, e.description(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun addListeners() {
        binding.btnDiaryDetailBack.setOnSingleClickListener {
            finish()
        }
        binding.btnDiaryDetailMenu.setOnSingleClickListener {
            DiaryDetailBottomSheet(viewModel).show(supportFragmentManager, DiaryDetailBottomSheet.TAG)
        }
    }

    override fun addObservers() {
        viewModel.isTopicExist.observe(this) {
            if (!it) binding.layoutDiaryDetailRandomTopic.layoutSection.visibility = View.GONE
        }
        viewModel.isDiaryDeleted.observe(this) {
            if (it) {
                Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }.run(::startActivity)
            }
        }
    }
}