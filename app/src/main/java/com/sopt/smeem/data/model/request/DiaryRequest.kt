package com.sopt.smeem.data.model.request

class DiaryRequest {
    data class Writing(
        val content: String,
        val topicId: Long?,
        val engKorExpression: String?
    )

    data class Editing(
        val content: String
    )
}
