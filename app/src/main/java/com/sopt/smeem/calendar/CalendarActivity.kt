package com.sopt.smeem.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.sopt.smeem.R
import com.sopt.smeem.calendar.util.TopSheetBehavior
import com.sopt.smeem.calendar.util.TopSheetBehavior.TopSheetCallback
import com.sopt.smeem.databinding.ActivityCalendarBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.dp
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

        binding.clCalendar.bringToFront()

        topSheetBehavior.setPeekHeight(168.dp(this))
        topSheetBehavior.setHideable(false)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.isHideable = false

        var isTopSheetInitiatedEvent = false

        topSheetBehavior.setTopSheetCallback(object : TopSheetCallback() {
            override fun onStateChanged(topSheet: View, newState: Int) {

                when (newState) {
                    TopSheetBehavior.STATE_HIDDEN -> {
                        Log.d("MainActivity", "state: hidden")
                    }

                    TopSheetBehavior.STATE_EXPANDED -> {
                        Log.d("MainActivity", "state: expanded")
                    }

                    TopSheetBehavior.STATE_COLLAPSED -> {
                        Log.d("MainActivity", "state: collapsed")
                    }

                    TopSheetBehavior.STATE_DRAGGING -> {
                        Log.d("MainActivity", "state: dragging")
                    }

                    TopSheetBehavior.STATE_SETTLING -> {
                        Log.d("MainActivity", "state: settling")
                    }

                }
            }

            override fun onSlide(topSheet: View, slideOffset: Float) {

                val fadeStartPosition = 0.0f
                val fadeEndPosition = 1.0f
                if (slideOffset in fadeStartPosition..fadeEndPosition) {
                    val alpha = 1 - slideOffset
                    weeklyCalendar.alpha = alpha
                }
                weeklyCalendar.isEnabled = slideOffset != fadeEndPosition
                weeklyCalendar.isClickable = slideOffset != fadeEndPosition

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