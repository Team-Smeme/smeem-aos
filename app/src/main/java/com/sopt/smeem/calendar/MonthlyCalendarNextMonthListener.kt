package com.sopt.smeem.calendar

import android.view.View

interface MonthlyCalendarNextMonthListener {
    fun onShowNextMonth(view: View, dateString: String)
}