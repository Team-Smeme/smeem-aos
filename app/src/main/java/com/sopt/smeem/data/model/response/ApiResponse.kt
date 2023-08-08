package com.sopt.smeem.data.model.response

data class ApiResponse<RESULT>(
    val status: Int?,
    val success: Boolean,
    val message: String?,
    val data: RESULT?,
)
