package com.sopt.smeem.presentation.calendar

import android.os.Bundle
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityHomeBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.calendar.listener.OnWeeklyCalendarSwipeListener
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeActivity : BindingActivity<ActivityHomeBinding>(R.layout.activity_home) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setInitListener()
        setTargetMonthTitle()
    }

    private fun setInitListener() {
        binding.weeklyCalendar.setOnWeeklyCalendarSwipeListener(object :
            OnWeeklyCalendarSwipeListener {
            override fun onSwipe(mondayDate: LocalDate?) {
                mondayDate?.let {
                    // TODO 서버 갱신
                    setTargetMonthTitle()
                }
            }
        })
    }

    private fun setTargetMonthTitle() {
        binding.tvTargetMonth.text = binding.weeklyCalendar.mondayDate?.format(
            DateTimeFormatter.ofPattern(TARGET_MONTH_PATTERN),
        )
    }

    companion object {
        const val TARGET_MONTH_PATTERN = "yyyy년 M월"
    }
}
