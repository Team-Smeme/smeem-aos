package com.sopt.smeem.presentation.bookmark.detail.contract

sealed class BookmarkDetailSideEffect {
    object NavigateBack : BookmarkDetailSideEffect()
    data class ShowError(val message: String) : BookmarkDetailSideEffect()
    object ShowMoreOptions : BookmarkDetailSideEffect()
    object NavigateToUseExpression : BookmarkDetailSideEffect()
    data class OpenInstagram(val url: String) : BookmarkDetailSideEffect()
    data class ShowToastAndNavigateToHome(val message: String) : BookmarkDetailSideEffect()
}