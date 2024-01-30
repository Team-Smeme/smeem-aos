package com.sopt.smeem.domain.model

import java.time.LocalTime

data class Diary(
    val id: Long? = null,
    val content: String,
    val topicId: Long? = null,
    val topic: String? = null,
    val createdAt: String? = null,
    val username: String? = null,
    val corrections: List<Correction>? = emptyList()
) {
    data class Correction(
        val id: Long,
        val before: String,
        val after: String,
    )
}
