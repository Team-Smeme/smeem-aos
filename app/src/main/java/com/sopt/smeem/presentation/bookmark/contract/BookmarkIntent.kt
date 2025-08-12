package com.sopt.smeem.presentation.bookmark.contract

sealed class BookmarkIntent {
    object LoadBookmarks : BookmarkIntent()
    data class OnBookmarkClick(val bookmarkId: Int) : BookmarkIntent()
}
