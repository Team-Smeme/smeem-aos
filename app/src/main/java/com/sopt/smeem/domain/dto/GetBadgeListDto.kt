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
    val remainingNumber: Int?,
)