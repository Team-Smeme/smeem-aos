package com.sopt.smeem.presentation.calendar.listener

import java.time.LocalDate

interface OnWeeklyCalendarSwipeListener {

    fun onSwipe(mondayDate: LocalDate?)
}
