package com.sopt.smeem.presentation.detail

import com.sopt.smeem.domain.dto.GetDiaryResponseDto
import com.sopt.smeem.util.DateUtil

data class DiaryDetail(
    val diaryId: Long,
    val topic: String?,
    val content: String,
    val createdAt: String,
    val writerUsername: String,
) {
    companion object {
        fun from(dto: GetDiaryResponseDto): DiaryDetail = with(dto) {
            DiaryDetail(
                diaryId = id,
                topic = topic,
                content = content,
                createdAt = DateUtil.asString(createdAt),
                writerUsername = username
            )
        }
    }
}
