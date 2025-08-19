package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.common.ApiResult
import com.sopt.smeem.domain.dto.GetBookmarkDetailResponseDto
import com.sopt.smeem.domain.dto.GetBookmarksResponseDto

interface BookmarkRepository {
    suspend fun getBookmarks(): ApiResult<GetBookmarksResponseDto>
    suspend fun getBookmarkDetail(bookmarkId: Int): ApiResult<GetBookmarkDetailResponseDto>
}