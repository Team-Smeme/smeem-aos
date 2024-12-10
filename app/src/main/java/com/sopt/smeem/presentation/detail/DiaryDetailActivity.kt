package com.sopt.smeem.presentation.detail

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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
        applyToggleButtonStyle(isCoachOn = false)
        binding.toggleCoach.check(R.id.btn_coach_off)
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

        binding.toggleCoach.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_coach_off -> {
                        applyToggleButtonStyle(isCoachOn = false)
                        eventVm.sendEvent(
                            AmplitudeEventType.MY_DIARY_TOGGLE_CLICK,
                            mapOf("toggle" to "off")
                        )
                        // 기존 XML 화면 표시
                        binding.scrollDiaryDetail.visibility = View.VISIBLE
                        binding.composeViewCoachDetail.visibility = View.GONE
                    }

                    R.id.btn_coach_on -> {
                        applyToggleButtonStyle(isCoachOn = true)
                        eventVm.sendEvent(
                            AmplitudeEventType.MY_DIARY_TOGGLE_CLICK,
                            mapOf("toggle" to "on")
                        )
                        // Compose 화면 표시
                        binding.scrollDiaryDetail.visibility = View.GONE
                        binding.composeViewCoachDetail.visibility = View.VISIBLE

                        // ComposeView 설정
                        binding.composeViewCoachDetail.setContent {
                            DiaryCoachingDetailScreen(
                                diaryDetail = viewModel.diaryDetailResult.value!!
                            )
                        }
                    }
                }
            }
        }
    }

    override fun addObservers() {
        viewModel.diaryDetailResult.observe(this) { diaryDetail ->
            binding.diaryDetail = diaryDetail
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

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBarLoading.visibility = View.VISIBLE
                binding.scrollDiaryDetail.visibility = View.GONE
                binding.composeViewCoachDetail.visibility = View.GONE
            } else {
                binding.progressBarLoading.visibility = View.GONE
                binding.scrollDiaryDetail.visibility = View.VISIBLE
                binding.composeViewCoachDetail.visibility = View.VISIBLE

                val diaryDetail = viewModel.diaryDetailResult.value
                if (diaryDetail != null) {
                    binding.toggleCoach.visibility =
                        if (!diaryDetail.hasCorrections || diaryDetail.isUpdated) View.GONE else View.VISIBLE
                }
            }
        }
    }

    private fun applyToggleButtonStyle(isCoachOn: Boolean) {
        if (isCoachOn) {
            // 코칭 ON 버튼 스타일 적용
            binding.btnCoachOn.setBackgroundColor(ContextCompat.getColor(this, R.color.point))
            binding.btnCoachOn.setTextColor(ContextCompat.getColor(this, R.color.white))
            // 코칭 OFF 버튼 스타일 적용
            binding.btnCoachOff.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.btnCoachOff.setTextColor(ContextCompat.getColor(this, R.color.gray_500))
        } else {
            // 코칭 OFF 버튼 스타일 적용
            binding.btnCoachOff.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_100))
            binding.btnCoachOff.setTextColor(ContextCompat.getColor(this, R.color.gray_500))
            binding.btnCoachOff.setStrokeColorResource(R.color.gray_200)
            // 코칭 ON 버튼 스타일 적용
            binding.btnCoachOn.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.btnCoachOn.setTextColor(ContextCompat.getColor(this, R.color.gray_500))
        }
    }
}
