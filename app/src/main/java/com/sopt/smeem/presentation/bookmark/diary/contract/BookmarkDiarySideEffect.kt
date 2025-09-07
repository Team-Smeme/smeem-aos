package com.sopt.smeem.presentation.bookmark.diary.contract

import com.sopt.smeem.domain.dto.RetrievedBadgeDto

sealed class BookmarkDiarySideEffect {
    object NavigateBack : BookmarkDiarySideEffect()
    data class NavigateToHomeWithSuccess(val diaryId: Long, val badges: List<RetrievedBadgeDto>) : BookmarkDiarySideEffect()
    data class ShowError(val message: String) : BookmarkDiarySideEffect()
}