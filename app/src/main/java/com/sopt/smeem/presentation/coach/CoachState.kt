package com.sopt.smeem.presentation.coach

import com.sopt.smeem.domain.dto.CorrectionDto
import com.sopt.smeem.presentation.detail.DiaryDetail
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class CoachState(
    val diaryId: Long = -1L,
    val initialDiaryContent: String = "",
    val diaryDetail: DiaryDetail = DiaryDetail(),
    val isLoading: Boolean = false,
    val corrections: PersistentList<CorrectionDto> = persistentListOf(),
) {
    val diaryContent: String get() = diaryDetail?.content ?: initialDiaryContent
    val createdAt: String get() = diaryDetail?.createdAt ?: ""
    val writerUsername: String get() = diaryDetail?.writerUsername ?: ""
    val topic: String? get() = diaryDetail?.topic
}
