package com.sopt.smeem.presentation.detail

import com.sopt.smeem.domain.dto.CorrectionDto
import com.sopt.smeem.domain.dto.GetDiaryResponseDto
import com.sopt.smeem.util.DateUtil

data class DiaryDetail(
    val diaryId: Long = -1L,
    val topic: String? = null,
    val content: String = "",
    val createdAt: String = "",
    val writerUsername: String = "",
    val corrections: List<CorrectionDto> = emptyList(),
    val correctionCount: Int = -1,
    val correctionMaxCount: Int = 0,
    val isUpdated: Boolean = false,
) {
    val hasCorrections: Boolean
        get() = corrections.isNotEmpty()

    companion object {
        fun from(dto: GetDiaryResponseDto): DiaryDetail = with(dto) {
            DiaryDetail(
                diaryId = id,
                topic = topic,
                content = content,
                createdAt = DateUtil.asString(createdAt),
                corrections = corrections,
                writerUsername = username,
                correctionCount = correctionCount,
                correctionMaxCount = correctionMaxCount,
                isUpdated = isUpdated
            )
        }
    }
}
