package com.sopt.smeem.presentation.bookmark.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface BookmarkRoute {
    @Serializable
    data object BookmarkList : BookmarkRoute

    @Serializable
    data class BookmarkDetail(val bookmarkId: Int) : BookmarkRoute
}