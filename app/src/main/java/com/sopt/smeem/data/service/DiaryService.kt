package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.DiaryRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.DiaryResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface DiaryService {
    @POST("/api/v2/diaries")
    suspend fun post(
        @Body request: DiaryRequest.Writing
    ): ApiResponse<Unit>

    @PATCH("/api/v2/diaries/{diaryId}")
    suspend fun patch(
        @Body request: DiaryRequest.Editing,
        @Query("diaryId") diaryId: Long
    ): ApiResponse<Unit>

    @DELETE("/api/v2/diaries/{diaryId}")
    suspend fun delete(
        @Query("diaryId") diaryId: Long
    ): ApiResponse<Unit>

    @GET("/api/v2/diaries/{diaryId}")
    suspend fun getDetail(
        @Query("diaryId") diaryId: Long
    ): ApiResponse<DiaryResponse.Detail>

    @GET("/api/v2/diaries")
    suspend fun getList(
        @Query("start") startDate: String,
        @Query("end") endDate: String
    ): ApiResponse<DiaryResponse.Diaries>

    @GET("/api/v2/topics/random")
    suspend fun getTopic(): ApiResponse<DiaryResponse.Topic>
}