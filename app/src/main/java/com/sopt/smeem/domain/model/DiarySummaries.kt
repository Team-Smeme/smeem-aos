package com.sopt.smeem.domain.model

data class DiarySummaries(
    val diaries: Map<Date, DiarySummary>,
    val has30Past: Boolean,
)