package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.DiaryRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.DiaryResponse
import com.sopt.smeem.data.model.response.DiaryWritingResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DiaryService {
    @POST("/api/v2/diaries")
    suspend fun post(
        @Body request: DiaryRequest.Writing
    ): ApiResponse<DiaryWritingResponse>

    @PATCH("/api/v2/diaries/{diaryId}")
    suspend fun patch(
        @Body request: DiaryRequest.Editing,
        @Path("diaryId") diaryId: Long
    ): ApiResponse<Unit>

    @DELETE("/api/v2/diaries/{diaryId}")
    suspend fun delete(
        @Path("diaryId") diaryId: Long
    ): ApiResponse<Unit>

    @GET("/api/v2/diaries/{diaryId}")
    suspend fun getDetail(
        @Path("diaryId") diaryId: Long
    ): ApiResponse<DiaryResponse.Detail>

    @GET("/api/v2/diaries")
    suspend fun getList(
        @Query("start") startDate: String, // yyyy-mm-dd
        @Query("end") endDate: String, // yyyy-mm-dd
    ): ApiResponse<DiaryResponse.Diaries>

    @GET("/api/v2/topics/random")
    suspend fun getTopic(): ApiResponse<DiaryResponse.Topic>
}