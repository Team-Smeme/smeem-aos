package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.BookmarkDetailResponse
import com.sopt.smeem.data.model.response.BookmarkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BookmarkService {
    @GET("/api/v2/bookmarks")
    suspend fun getBookmarks(): Response<ApiResponse<BookmarkResponse>>
    
    @GET("/api/v2/bookmarks/detail/{bookmarkId}")
    suspend fun getBookmarkDetail(
        @Path("bookmarkId") bookmarkId: Int
    ): Response<ApiResponse<BookmarkDetailResponse>>
}