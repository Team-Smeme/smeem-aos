package com.sopt.smeem.domain.dto

data class WriteDiaryRequestDto(
    val content: String,
    val topicId: Long? = null,
    val engKorExpression: String? = null,
)