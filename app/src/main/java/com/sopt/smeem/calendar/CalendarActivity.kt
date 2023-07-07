package com.sopt.smeem.calendar

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.sopt.smeem.R
import com.sopt.smeem.calendar.util.TopSheetBehavior
import com.sopt.smeem.calendar.util.TopSheetBehavior.TopSheetCallback
import com.sopt.smeem.databinding.ActivityCalendarBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.dp
import com.sopt.smeem.util.verticalSliding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarActivity : BindingActivity<ActivityCalendarBinding>(R.layout.activity_calendar) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initializePersistentBottomSheet()
    }

    private fun initializePersistentBottomSheet() {
        val topSheet = binding.integratedCalendar
        val bottomSheet = binding.clHomeBottom
        val topSheetBehavior = TopSheetBehavior.from(topSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        val weeklyCalendar = topSheet.getWeeklyCalendar()
        val monthlyCalendar = topSheet.getMonthlyCalendar()

        binding.clCalendar.bringToFront()

        topSheetBehavior.setPeekHeight(168.dp(this))
        topSheetBehavior.setHideable(false)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.isHideable = false

        topSheetBehavior.setTopSheetCallback(object : TopSheetCallback() {
            override fun onStateChanged(topSheet: View, newState: Int) {
                when (newState) {
                    TopSheetBehavior.STATE_EXPANDED -> {
                        weeklyCalendar.visibility = GONE
                        monthlyCalendar.visibility = VISIBLE
                    }

                    TopSheetBehavior.STATE_COLLAPSED -> {
                        monthlyCalendar.visibility = INVISIBLE
                        weeklyCalendar.visibility = VISIBLE
                    }

                    TopSheetBehavior.STATE_DRAGGING -> {
                        weeklyCalendar.visibility = VISIBLE
                        monthlyCalendar.visibility = VISIBLE
                    }

                    TopSheetBehavior.STATE_SETTLING -> {
                        weeklyCalendar.visibility = GONE
                    }
                }
            }

            override fun onSlide(topSheet: View, slideOffset: Float) {
                weeklyCalendar.verticalSliding(slideOffset)

                val bottomSheetOffset = bottomSheet.height * slideOffset
                bottomSheet.y = bottomSheetOffset
            }
        })

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 바텀시트를 드래그했을 때 아무 동작도 하지 않음
            }
        })
    }
}
