package com.sopt.smeem.data.model.response

class DiaryResponse {
    data class Detail(
        val diaryId: Long,
        val topic: String?,
        val content: String,
        val createdAt: String,
        val username: String,
        val correctionResponses: List<CorrectionResponse>?
    ) {
        data class CorrectionResponse(
            val correctionId: Long,
            val before: String,
            val after: String
        )
    }

    data class Summary(
        val diaryId: Long,
        val content: String,
        val createdAt: String,
    )

    data class Diaries(
        val diaries: List<Summary> = emptyList(),
        val has30Past: Boolean
    )
}