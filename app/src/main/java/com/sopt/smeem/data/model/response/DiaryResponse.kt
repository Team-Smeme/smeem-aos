package com.sopt.smeem.data.model.response

class DiaryResponse {
    data class Detail(
        val diaryId: Long,
        val topic: String?,
        val content: String,
        val createdAt: String,
        val username: String,
        val corrections: List<CorrectionResponse>?
    ) {
        data class CorrectionResponse(
            val correctionId: Long,
            val before: String,
            val after: String
        )
    }

    // calendar related
    data class Summary(
        val diaryId: Long,
        val content: String,
        val createdAt: String,
    )

    // calendar related
    data class Diaries(
        val diaries: List<Summary> = emptyList(),
        val has30Past: Boolean
    )

    data class Topic(
        val topicId: Long,
        val content: String
    )
}