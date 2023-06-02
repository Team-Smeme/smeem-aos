package com.sopt.smeem.calendar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sopt.smeem.R
import com.sopt.smeem.calendar.monthly.MonthlyCalendar
import com.sopt.smeem.calendar.weekly.WeeklyCalendar
import com.sopt.smeem.databinding.ActivityCalendarBinding

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topSheet = findViewById<CoordinatorLayout>(R.id.top_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(topSheet)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Bottom Sheet의 상태가 변경될 때
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val weeklyCalendar = findViewById<WeeklyCalendar>(R.id.week_calendar)
                val monthlyCalendar = findViewById<MonthlyCalendar>(R.id.month_calendar)

                if (slideOffset > 0.5) {
                    // MonthlyCalendar를 표시하고 WeeklyCalendar를 숨깁니다.
                    // 0.5 값을 기준으로 바꾸려면 원하는 값으로 조정하십시오.
                    weeklyCalendar.visibility = View.GONE
                    monthlyCalendar.visibility = View.VISIBLE
                } else {
                    // WeeklyCalendar를 표시하고 MonthlyCalendar를 숨깁니다.
                    weeklyCalendar.visibility = View.VISIBLE
                    monthlyCalendar.visibility = View.GONE
                }
            }
        })
    }

}