package com.sopt.smeem.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sopt.smeem.R
import com.sopt.smeem.calendar.util.TopSheetBehavior
import com.sopt.smeem.calendar.util.TopSheetBehavior.TopSheetCallback
import com.sopt.smeem.calendar.weekly.WeeklyCalendar
import com.sopt.smeem.databinding.ActivityCalendarBinding
import com.sopt.smeem.util.dp

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializePersistentBottomSheet()
    }



    private fun initializePersistentBottomSheet() {
        val topSheet = binding.topSheetView
        val topSheetBehavior = TopSheetBehavior.from(topSheet)
        val weeklyCalendar = topSheet.getWeeklyCalendar()

        binding.clCalendar.bringToFront()

        topSheetBehavior.setPeekHeight(168.dp(this))
        topSheetBehavior.setHideable(false)



        topSheetBehavior.setTopSheetCallback(object : TopSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
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

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.llCalendar.translationY = bottomSheet.height * slideOffset

                val fadeStartPosition = 0.0f
                val fadeEndPosition = 1.0f
                if (slideOffset in fadeStartPosition..fadeEndPosition) {
                    val alpha = 1 - slideOffset
                    weeklyCalendar.alpha = alpha
                }
                weeklyCalendar.isClickable = slideOffset != fadeEndPosition
            }

        })

    }
}