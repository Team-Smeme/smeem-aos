package com.sopt.smeem.data.model.response

import com.sopt.smeem.domain.model.BadgeType

data class BadgeTypeResponse(
    val badgeType: BadgeType,
    val badgeTypeName: String,
    val badges: List<BadgeResponse>
) {
    data class BadgeResponse(
        val name: String,
        val type: BadgeType,
        val imageUrl: String,
    )
}
