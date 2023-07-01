package com.sopt.smeem.calendar.monthly.listener

import android.view.View
import java.time.LocalDate

interface OnMonthlyCalendarSwipeListener {

    fun onSwipe(dateString: String)
}