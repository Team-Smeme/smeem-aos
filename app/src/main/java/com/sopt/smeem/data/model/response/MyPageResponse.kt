package com.sopt.smeem.data.model.response

import com.sopt.smeem.Day

data class MyPageResponse(
    val username: String,
    val target: String,
    val targetHowTo: String?,
    val targetDetail: String?,
    val targetLang: String,
    val hasPushAlarm: Boolean,
    val trainingTime: TrainingResponse,
    val badge: BadgeResponse
) {
    data class TrainingResponse(
        val day: Set<Day>,
        val hour: String,
        val minute: String
    )

    data class BadgeResponse(
        val id: Long,
        val name: String,
        val type: String,
        val imageUrl: String
    )
}
