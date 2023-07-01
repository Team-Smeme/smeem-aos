package com.sopt.smeem.presentation.home

import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityHomeBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.auth.splash.WritingBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class HomeActivity : BindingActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private val vm: HomeViewModel by viewModels()
    lateinit var bs: WritingBottomSheet

    override fun constructLayout() {
        setUpWritingBottomSheet()
        vm.getContents(
            LocalDateTime.now(),
            onError = { e -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() }
        )

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)

        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                moveButton(newState)
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    override fun addListeners() {
        dateFlagChanged()
        listenDiarySummaries()
        onTouchWriting()
    }

    private fun onTouchWriting() {
        binding.btnHomeBottomWriting.setOnClickListener {
            bs.show(supportFragmentManager, "tag")
        }
    }

    private fun setUpWritingBottomSheet() {
        bs = WritingBottomSheet()
    }

    private fun dateFlagChanged() {
        // Calendar 의 날짜 선택을 통해 변경이 감지되면, daily 일기 반영이 되어야함
        // (1) 오늘 날짜 기준으로 조회된 Calendar 데이터 range 내에 속한 값인지 확인

        // (2) range 내에 있다면 해당 값 노출 (null 인 경우 => 작성한 일기가 없어요.)

        // (3) range 내 없는 값이라면 해당날짜 조회 (null 인 경우 => 작성한 일기가 없어요.)

    }

    private fun listenDiarySummaries() {
        vm.diarySummaries.observe(this) {
            setMonthlyCalendar()
            setWeeklyCalendar()
            setDailyDiary()
        }
    }

    private fun setMonthlyCalendar() {

    }

    private fun setWeeklyCalendar() {

    }

    private fun setDailyDiary() {

    }

    private fun moveButton(state: Int) {
        /*        val onBottomSheet = findViewById<ConstraintLayout>(R.id.included_bottom_sheet)
                val onBottomSheetButton = onBottomSheet.findViewById<Button>(R.id.btn_home_bottom_writing)
                val bottomSheetButtonLayoutParams = onBottomSheetButton.layoutParams as ConstraintLayout.LayoutParams

                when(state) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        bottomSheetButtonLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                        onBottomSheetButton.layoutParams = bottomSheetButtonLayoutParams
                        bottomSheetButtonLayoutParams.topToBottom = ConstraintLayout.LayoutParams.UNSET
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        bottomSheetButtonLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                        bottomSheetButtonLayoutParams.topToBottom = findViewById<ConstraintLayout>(R.id.layout_home_bottom_no_diary).id
                        onBottomSheetButton.layoutParams = bottomSheetButtonLayoutParams

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        bottomSheetButtonLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                        bottomSheetButtonLayoutParams.topToBottom = findViewById<ConstraintLayout>(R.id.layout_home_bottom_no_diary).id
                        onBottomSheetButton.layoutParams = bottomSheetButtonLayoutParams
                    }
                }*/
    }
}