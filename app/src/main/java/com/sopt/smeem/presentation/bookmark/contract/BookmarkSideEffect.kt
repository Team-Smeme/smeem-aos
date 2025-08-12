package com.sopt.smeem.presentation.bookmark.contract

sealed class BookmarkSideEffect {
    data class NavigateToDetail(val bookmarkId: Int) : BookmarkSideEffect()
    data class ShowError(val message: String) : BookmarkSideEffect()
}
