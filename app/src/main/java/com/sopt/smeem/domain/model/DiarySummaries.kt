package com.sopt.smeem.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class DiarySummaries(
    val diaries: Map<LocalDate, DiarySummary>,
    val has30Past: Boolean
)

data class DiarySummary(
    val id: Long,
    val content: String,
    val createdAt: LocalTime,
)