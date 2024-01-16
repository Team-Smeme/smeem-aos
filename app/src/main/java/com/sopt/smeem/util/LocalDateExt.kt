package com.sopt.smeem.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

internal fun LocalDate.toYearMonth(): YearMonth = YearMonth.of(this.year, this.month)

internal fun LocalDate.getNextDates(count: Int): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    repeat(count) { day ->
        dates.add(this.plusDays(day.toLong()))
    }
    return dates
}

internal fun LocalDate.getWeekStartDate(start: DayOfWeek = DayOfWeek.SUNDAY): LocalDate {
    var date = this
    while (date.dayOfWeek != start) {
        date = date.minusDays(1)
    }
    return date
}

internal fun LocalDate.getMonthStartDate(): LocalDate =
    LocalDate.of(this.year, this.month, 1)

internal fun LocalDate.getRemainingDatesInWeek(start: DayOfWeek = DayOfWeek.SUNDAY): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    var date = this.plusDays(1)
    while (date.dayOfWeek != start) {
        dates.add(date)
        date = date.plusDays(1)
    }
    return dates
}

internal fun LocalDate.getRemainingDatesInMonth(): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    repeat(this.month.length(this.isLeapYear) - this.dayOfMonth + 1) {
        dates.add(this.plusDays(it.toLong()))
    }
    return dates
}