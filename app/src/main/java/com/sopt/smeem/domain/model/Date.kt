package com.sopt.smeem.domain.model

import java.time.DayOfWeek
import java.time.LocalDateTime

data class Date(
    val year: Int,
    val month: Int,
    val day: Int,
    val dayOfWeek: DayOfWeek? = null,
    val hour: Int? = null,
    val minute: Int? = null,
    val second: Int? = null,
    val mSecond: Int? = null,
    val nSecond: Int? = null
) {
    companion object {
        fun from(now: LocalDateTime) = Date(
            year = now.year,
            month = now.monthValue,
            day = now.dayOfMonth,
            dayOfWeek = now.dayOfWeek,
            hour = now.hour,
            minute = now.minute,
            second = now.second,
        )
    }
}
