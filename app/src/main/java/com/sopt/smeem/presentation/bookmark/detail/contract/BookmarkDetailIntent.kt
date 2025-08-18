package com.sopt.smeem.presentation.bookmark.detail.contract

sealed class BookmarkDetailIntent {
    data class LoadBookmarkDetail(val bookmarkId: Int) : BookmarkDetailIntent()
    object OnBackClick : BookmarkDetailIntent()
    object OnMoreClick : BookmarkDetailIntent()
    object OnUseExpressionClick : BookmarkDetailIntent()
    object OnInstagramClick : BookmarkDetailIntent()
}