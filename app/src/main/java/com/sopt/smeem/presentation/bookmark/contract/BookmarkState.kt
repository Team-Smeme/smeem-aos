package com.sopt.smeem.presentation.bookmark.contract

data class BookmarkState(
    val isLoading: Boolean = false,
    val bookmarks: List<BookmarkItem> = emptyList(),
    val error: String? = null
)

data class BookmarkItem(
    val bookmarkId: Int,
    val thumbnailImageUrl: String,
    val expression: String,
    val type: BookmarkType,
    val description: String,
    val createdAt: String
)

enum class BookmarkType {
    REELS, P;
    
    companion object {
        fun fromString(scrapType: String?): BookmarkType {
            return when (scrapType) {
                "REELS" -> REELS
                else -> P // null이거나 다른 값일 경우 P로 기본값 설정
            }
        }
    }
}
