package com.sopt.smeem.calendar.weekly.listener

import java.time.LocalDate

interface OnWeeklyCalendarSwipeListener {

    fun onSwipe(mondayDate: LocalDate?)
}