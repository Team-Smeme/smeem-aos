package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.common.ApiResult
import com.sopt.smeem.domain.dto.CreateBookmarkResponseDto
import com.sopt.smeem.domain.dto.GetBookmarkDetailResponseDto
import com.sopt.smeem.domain.dto.GetBookmarksResponseDto

interface BookmarkRepository {
    suspend fun getBookmarks(): ApiResult<GetBookmarksResponseDto>
    suspend fun getBookmarkDetail(bookmarkId: Int): ApiResult<GetBookmarkDetailResponseDto>
    suspend fun createBookmark(url: String): ApiResult<CreateBookmarkResponseDto>
}