package com.sopt.smeem.domain.dto

data class GetBookmarksResponseDto(
    val bookmarks: List<BookmarkDto>
)

data class BookmarkDto(
    val bookmarkId: Int?,
    val thumbnailImageUrl: String?,
    val expression: String?,
    val description: String?,
    val createdAt: String?,
    val scrapType: String?
)

data class GetBookmarkDetailResponseDto(
    val thumbnailImageUrl: String,
    val scrapedUrl: String,
    val expression: String,
    val translatedExpression: String,
    val description: String,
    val scrapType: String
)

data class CreateBookmarkResponseDto(
    val thumbnailImageUrl: String,
    val scrapedUrl: String,
    val expression: String?,
    val translatedExpression: String,
    val description: String,
    val scrapType: String,
    val scrapedCountPerDay: Int
)