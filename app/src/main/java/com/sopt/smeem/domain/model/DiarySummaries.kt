package com.sopt.smeem.domain.model

data class DiarySummaries(
    val diaries: Map<String, DiarySummary>,
    val has30Past: Boolean,
)
