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
    val isSurvey: Boolean = true,
    val username: String = "",
    val totalCount: Int = 0,
    val corrections: PersistentList<CorrectionDto> = persistentListOf(),
    val thumbSelection: ThumbSelection = ThumbSelection.NONE
) {
    val diaryContent: String get() = diaryDetail.content
    val createdAt: String get() = diaryDetail.createdAt
    val writerUsername: String get() = diaryDetail.writerUsername
    val topic: String? get() = diaryDetail.topic
    val isCoachEnabled: Boolean get() = diaryDetail.correctionCount < diaryDetail.correctionMaxCount
}

enum class ThumbSelection {
    NONE, THUMB_UP, THUMB_DOWN
}
