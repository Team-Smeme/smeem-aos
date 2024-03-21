package com.sopt.smeem.domain.model

import com.sopt.smeem.data.model.response.MyPageResponse

data class MyPageBadge(
    val badgeId: Long,
    val title: String,
    val description: String?,
    val badgeType: String,
    val imageUrl: String?
) {
    companion object {
        fun from(myPageBadgeResponse: MyPageResponse.MyPageBadgeResponse) =
            MyPageBadge(
                badgeId = myPageBadgeResponse.id,
                title = myPageBadgeResponse.name,
                description = myPageBadgeResponse.description,
                imageUrl = myPageBadgeResponse.imageUrl,
                badgeType = myPageBadgeResponse.type
            )
    }
}
