package com.sopt.smeem.domain.model

data class Diary(
    val id: Long? = null,
    val content: String,
    val topicId: Long? = null,
    val topic: String? = null,
    val createdAt: String? = null,
    val username: String,
    val corrections: List<Correction>? = emptyList(),
) {
    data class Correction(
        val id: Long,
        val before: String,
        val after: String,
    )
}

data class DiarySummary(
    val id: Long,
    val content: String,
    val createdAt: String,
)
