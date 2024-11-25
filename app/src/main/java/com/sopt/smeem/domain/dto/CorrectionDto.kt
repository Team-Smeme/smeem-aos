package com.sopt.smeem.domain.dto

data class CorrectionDto(
    val originalSentence: String,
    val correctedSentence: String,
    val reason: String?,
    val isCorrected: Boolean,
)
