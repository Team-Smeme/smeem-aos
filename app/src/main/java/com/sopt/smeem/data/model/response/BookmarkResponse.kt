package com.sopt.smeem.data.model.response

data class BookmarkResponse(
    val bookmarks: List<BookmarkItem>?
) {
    data class BookmarkItem(
        val bookmarkId: Int?,
        val thumbnailImageUrl: String?,
        val expression: String?,
        val description: String?,
        val createdAt: String?,
        val scrapType: String?
    )
}