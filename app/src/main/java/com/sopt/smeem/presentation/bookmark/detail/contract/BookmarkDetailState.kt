package com.sopt.smeem.presentation.bookmark.detail.contract

data class BookmarkDetailState(
    val isLoading: Boolean = false,
    val bookmarkDetail: BookmarkDetailItem? = null,
    val error: String? = null,
    val isFromDeepLink: Boolean = false
)

data class BookmarkDetailItem(
    val thumbnailImageUrl: String,
    val scrapedUrl: String,
    val expression: String,
    val translatedExpression: String,
    val description: String,
    val scrapType: String
)