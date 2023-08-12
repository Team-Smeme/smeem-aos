package com.sopt.smeem.domain.model

import com.sopt.smeem.data.model.response.MyPageResponse

data class Badge(
    val badgeId: Long,
    val title: String,
    val description: String?,
    val badgeType: BadgeType,
    val imageUrl: String?
) {
    companion object {
        fun from(badgeResponse: MyPageResponse.BadgeResponse) =
            Badge(
                badgeId = badgeResponse.id,
                title = badgeResponse.name,
                description = badgeResponse.description,
                imageUrl = badgeResponse.imageUrl,
                badgeType = badgeResponse.type
            )
    }
}
