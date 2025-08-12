package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.BookmarkResponse
import retrofit2.Response
import retrofit2.http.GET

interface BookmarkService {
    @GET("/api/v2/bookmarks")
    suspend fun getBookmarks(): Response<ApiResponse<BookmarkResponse>>
}