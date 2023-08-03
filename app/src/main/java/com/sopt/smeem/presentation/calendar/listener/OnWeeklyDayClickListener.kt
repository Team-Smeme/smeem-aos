package com.sopt.smeem.presentation.calendar.listener

import android.view.View
import java.time.LocalDate

fun interface OnWeeklyDayClickListener {
    fun onWeeklyDayClick(view: View, localDate: LocalDate)
}
