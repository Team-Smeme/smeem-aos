package com.sopt.smeem.data.model.response

import com.sopt.smeem.domain.model.BadgeType

data class MyPageResponse(
    val username: String,
    val title: String,
    val target: String,
    val way: String?,
    val detail: String?,
    val targetLang: String,
    val hasPushAlarm: Boolean,
    val trainingTime: TrainingResponse?,
    val badge: MyPageBadgeResponse
) {
    data class TrainingResponse(
        val day: String,
        val hour: Int,
        val minute: Int
    )

    data class MyPageBadgeResponse(
        val id: Long,
        val name: String,
        val type: String,
        val imageUrl: String,
        val description: String?
    )
}
