package com.sopt.smeem.calendar.monthly.listener

import android.view.View
import java.time.LocalDate

interface OnMonthlyDayClickListener {
    fun onWeeklyDayClick(view: View, localDate:LocalDate)
}