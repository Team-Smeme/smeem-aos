package com.sopt.smeem.domain.model

import java.time.LocalDate

data class Date(
    val day: LocalDate,
    val isCurrentMonth: Boolean,
    val isDiaryWritten: Boolean
)