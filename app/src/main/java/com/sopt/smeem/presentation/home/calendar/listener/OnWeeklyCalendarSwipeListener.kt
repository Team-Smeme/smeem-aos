package com.sopt.smeem.presentation.home.calendar.listener

import java.time.LocalDate

interface OnWeeklyCalendarSwipeListener {

    fun onSwipe(mondayDate: LocalDate?)
}
