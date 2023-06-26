package com.sopt.smeem.data.model.response

import com.sopt.smeem.calendar.Day
import com.sopt.smeem.domain.model.BadgeType

data class MyPageResponse(
    val username: String,
    val target: String,
    val way: String?,
    val detail: String?,
    val targetLang: String,
    val hasPushAlarm: Boolean,
    val trainingTime: TrainingResponse,
    val badges: List<BadgeResponse>
) {
    data class TrainingResponse(
        val day: Set<Day>,
        val hour: String,
        val minute: String
    )

    data class BadgeResponse(
        val id: Long,
        val name: String,
        val type: BadgeType,
        val imageUrl: String,
        val description: String?
    )
}
