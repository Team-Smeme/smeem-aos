package com.sopt.smeem.presentation.detail

import com.sopt.smeem.domain.dto.GetDiaryResponseDto
import com.sopt.smeem.util.DateUtil

data class DiaryDetail(
    val diaryId: Long = -1L,
    val topic: String? = null,
    val content: String = "",
    val createdAt: String = "",
    val writerUsername: String = "",
    val correctionCount: Int = -1,
    val correctionMaxCount: Int = 0,
) {
    companion object {
        fun from(dto: GetDiaryResponseDto): DiaryDetail = with(dto) {
            DiaryDetail(
                diaryId = id,
                topic = topic,
                content = content,
                createdAt = DateUtil.asString(createdAt),
                writerUsername = username,
                correctionCount = correctionCount,
                correctionMaxCount = correctionMaxCount,
            )
        }
    }
}
