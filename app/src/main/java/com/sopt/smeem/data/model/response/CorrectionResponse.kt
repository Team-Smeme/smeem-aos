package com.sopt.smeem.data.model.response

data class PostCorrectionResponse(
    val corrections: List<CorrectionResponse>,
)

data class CorrectionResponse(
    val original_sentence: String,
    val corrected_sentence: String,
    val reason: String?,
    val is_corrected: Boolean,
)
