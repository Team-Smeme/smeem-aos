package com.sopt.smeem.presentation.coach

import com.sopt.smeem.presentation.detail.DiaryDetail

data class CoachState(
    val diaryId: Long = -1L,
    val initialDiaryContent: String = "",
    val diaryDetail: DiaryDetail? = null,
    val isLoading: Boolean = false,
) {
    val diaryContent: String get() = diaryDetail?.content ?: initialDiaryContent
    val createdAt: String get() = diaryDetail?.createdAt ?: ""
    val writerUsername: String get() = diaryDetail?.writerUsername ?: ""
    val topic: String? get() = diaryDetail?.topic
}
