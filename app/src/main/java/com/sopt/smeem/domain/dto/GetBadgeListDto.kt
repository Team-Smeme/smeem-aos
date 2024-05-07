package com.sopt.smeem.domain.dto

import com.sopt.smeem.domain.model.BadgeType

data class GetBadgeListDto(
    val badgeId: Long,
    val name: String,
    val type: BadgeType,
    val hasBadge: Boolean,
    val remainingNumber: Int?,
    val contentForNonBadgeOwner: String,
    val contentForBadgeOwner: String,
    val imageUrl: String,
    val badgeAcquisitionRatio: Double,
) {
    fun getAcquistionText() = run { "$badgePercentage%의 사용자가 획득했어요" }

    fun getNonBadgeTextParts(): List<String> {
        val text = contentForNonBadgeOwner.split("n")
        return listOf(
            text.firstOrNull() ?: "",
            remainingNumber.toString(),
            text.lastOrNull() ?: ""
        )
    }

    val badgePercentage =
        // 웰컴 배지는 100%
        if (badgeId == 1.toLong()) {
            100
        } else {
            val percentage = badgeAcquisitionRatio * 100
            if (percentage == percentage.toInt().toDouble()) {
                percentage.toInt()
            } else {
                percentage
            }
        }
}