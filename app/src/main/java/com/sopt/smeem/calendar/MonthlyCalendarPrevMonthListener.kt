package com.sopt.smeem.calendar

import android.view.View

interface MonthlyCalendarPrevMonthListener {
    fun onShowPrevMonth(view: View, dateString: String)
}