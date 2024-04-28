package com.sopt.smeem.data.model.response

import com.sopt.smeem.domain.model.BadgeType

data class BadgeResponse(
    val badgeId: Long,
    val name: String,
    val type: BadgeType,
    val hasBadge: Boolean,
    val contentForNonBadgeOwner: String,
    val contentForBadgeOwner: String,
    val imageUrl: String,
    val badgeAcquisitionRatio: Double,
    val remainingNumber: Int?,
)