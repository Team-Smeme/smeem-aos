package com.sopt.smeem.domain.model

import com.sopt.smeem.data.model.response.MyPageResponse

data class Badge(
    val badgeId: Long,
    val title: String,
    val description: String,
    val badgeType: BadgeType,
    val imageUrl: String = "https://github-production-user-asset-6210df.s3.amazonaws.com/120551217/244917999-f9f63817-8b1a-483c-b0e1-4935c96e2d03.png"
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
