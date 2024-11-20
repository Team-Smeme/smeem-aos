package com.sopt.smeem.domain.dto

data class CorrectionDto(
    val original_sentence: String,
    val corrected_sentence: String,
    val reason: String?,
    val is_corrected: Boolean,
)
