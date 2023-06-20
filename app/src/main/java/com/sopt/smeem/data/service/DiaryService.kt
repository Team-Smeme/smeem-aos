package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.DiaryRequest
import com.sopt.smeem.data.model.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface DiaryService {
    @POST("/api/v2/diaries")
    suspend fun post(
        @Body request: DiaryRequest.Writing
    ): ApiResponse<Unit>

    @PATCH("/api/v2/diaries/{diaryId}")
    abstract fun patch(
        @Body request: DiaryRequest.Editing,
        @Query("diaryId") diaryId: Long
    ): ApiResponse<Unit>
}