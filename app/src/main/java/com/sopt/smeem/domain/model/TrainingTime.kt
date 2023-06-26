package com.sopt.smeem.domain.model

import com.sopt.smeem.calendar.Day
data class TrainingTime(
    val days: Set<Day>,
    val hour: Int,
    val minute: Int
) {
    fun isSet() = days.isNotEmpty()
}