package com.sopt.smeem.domain.model

import java.time.DayOfWeek
import java.time.MonthDay
import java.time.Year

data class Date(
    val year: Year,
    val monthDay: MonthDay,
    val dayOfWeek: DayOfWeek,
    val hour: Int,
    val minute: Int,
    val second: Int? = null,
    val mSecond: Int? = null,
    val nSecond: Int? = null
) {
    val month by lazy { monthDay.month }
    val day by lazy { monthDay.dayOfMonth }
}
