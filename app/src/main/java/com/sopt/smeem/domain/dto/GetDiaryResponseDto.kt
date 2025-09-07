package com.sopt.smeem.domain.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class GetDiaryResponseDto(
    @SerializedName("diaryId") val id: Long,
    val content: String,
    val createdAt: LocalDateTime,
    val username: String,
    val topic: String? = null,
    val corrections: List<CorrectionDto> = emptyList(),
    val correctionCount: Int = -1,
    val correctionMaxCount: Int = -1,
    val isUpdated: Boolean = false,
    val engKorExpression: String? = null
)
