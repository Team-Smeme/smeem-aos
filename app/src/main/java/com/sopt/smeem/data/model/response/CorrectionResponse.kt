package com.sopt.smeem.data.model.response

data class PostCorrectionResponse(
    val corrections: List<CorrectionResponse>,
)

data class CorrectionResponse(
    val originalSentence: String,
    val correctedSentence: String,
    val reason: String?,
    val isCorrected: Boolean,
)
