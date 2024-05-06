package com.sopt.smeem.domain.dto

import com.sopt.smeem.domain.model.BadgeType

data class GetBadgeListDto(
    val badgeId: Long,
    val name: String,
    val type: BadgeType,
    val hasBadge: Boolean,
    val contentForNonBadgeOwner: String,
    val contentForBadgeOwner: String,
    val imageUrl: String,
    val badgeAcquisitionRatio: Double,
) {
    fun getAcquistionText() = run { "${badgeRatio}%의 사용자가 획득했어요" }

    val badgeRatio =
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