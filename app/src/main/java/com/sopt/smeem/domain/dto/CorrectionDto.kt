package com.sopt.smeem.domain.dto

data class CorrectionsResponseDto(
    val corrections: List<CorrectionDto>,
    val username: String?,
    val totalCount: Int?,
)

data class CorrectionDto(
    val originalSentence: String,
    val correctedSentence: String,
    val reason: String?,
    val isCorrected: Boolean,
)
