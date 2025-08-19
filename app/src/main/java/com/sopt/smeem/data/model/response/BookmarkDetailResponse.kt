package com.sopt.smeem.data.model.response

data class BookmarkDetailResponse(
    val thumbnailImageUrl: String?,
    val scrapedUrl: String?,
    val expression: String?,
    val translatedExpression: String?,
    val description: String?,
    val scrapType: String?
)