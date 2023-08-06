package com.sopt.smeem.data.model.response

data class DiaryWritingResponse(
    val diaryId: Long,
    val badges: List<RetrievedBadgeResponse>
) {
    data class RetrievedBadgeResponse(
        val name: String,
        val imageUrl: String,
    )
}
