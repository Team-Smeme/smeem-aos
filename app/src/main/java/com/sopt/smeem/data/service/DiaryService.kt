package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.DiaryRequest
import com.sopt.smeem.data.model.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface DiaryService {
    @POST("/api/v2/diaries")
    suspend fun post(
        @Body request: DiaryRequest.Writing
    ): ApiResponse<Unit>
}