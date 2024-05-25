package com.sopt.smeem.presentation.detail

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityDiaryDetailBinding
import com.sopt.smeem.event.AmplitudeEventType
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.presentation.IntentConstants.DIARY_ID
import com.sopt.smeem.presentation.base.BindingActivity
import com.sopt.smeem.presentation.home.HomeActivity
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryDetailActivity :
    BindingActivity<ActivityDiaryDetailBinding>(R.layout.activity_diary_detail) {

    private val viewModel by viewModels<DiaryDetailViewModel>()
    private val eventVm: EventVM by viewModels()

    override fun constructLayout() {
        // databinding
        binding.lifecycleOwner = this
        // data
        viewModel.setDiaryId(intent.getLongExtra(DIARY_ID, -1))
        viewModel.getDiaryDetail { t ->
            Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
        }

        eventVm.sendEvent(AmplitudeEventType.MY_DIARY_CLICK)
    }

    override fun addListeners() {
        binding.btnDiaryDetailBack.setOnSingleClickListener {
            finish()
        }
        binding.btnDiaryDetailMenu.setOnSingleClickListener {
            DiaryDetailBottomSheet(viewModel, eventVm).show(
                supportFragmentManager,
                DiaryDetailBottomSheet.TAG
            )
        }
    }

    override fun addObservers() {
        viewModel.diaryDetailResult.observe(this) {
            binding.diaryDetail = it
        }

        viewModel.isTopicExist.observe(this) {
            if (!it) binding.layoutDiaryDetailRandomTopic.layoutSection.visibility = View.GONE
        }
        viewModel.isDiaryDeleted.observe(this) {
            if (it) {
                Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }.run(::startActivity)
            }
        }
    }
}