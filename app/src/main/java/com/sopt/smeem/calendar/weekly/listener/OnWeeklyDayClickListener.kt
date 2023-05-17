package com.sopt.smeem.calendar.weekly.listener

import android.view.View
import java.time.LocalDate

interface OnWeeklyDayClickListener {
    fun onWeeklyDayClick(view: View, localDate:LocalDate)
}