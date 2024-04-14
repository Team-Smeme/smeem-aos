package com.sopt.smeem.presentation.home.calendar.core

import java.time.LocalDate

sealed class CalendarState {
    class LoadNextDates(
        val startDate: LocalDate,
        val period: Period = Period.WEEK
    ) : CalendarState()

    class SelectDate(val date: LocalDate) : CalendarState()

    object ExpandCalendar : CalendarState()
    object CollapseCalendar : CalendarState()
}