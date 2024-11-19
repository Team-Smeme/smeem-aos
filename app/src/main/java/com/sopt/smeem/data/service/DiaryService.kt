package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.DiaryRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.DiaryResponse
import com.sopt.smeem.data.model.response.DiaryWritingResponse
import com.sopt.smeem.data.model.response.PostCorrectionResponse
import retrofit2.Response
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
    ): Response<ApiResponse<DiaryWritingResponse>>

    @PATCH("/api/v2/diaries/{diaryId}")
    suspend fun patch(
        @Body request: DiaryRequest.Editing,
        @Path("diaryId") diaryId: Long
    ): Response<ApiResponse<Unit>>

    @DELETE("/api/v2/diaries/{diaryId}")
    suspend fun delete(
        @Path("diaryId") diaryId: Long
    ): Response<ApiResponse<Unit>>

    @GET("/api/v2/diaries/{diaryId}")
    suspend fun getDetail(
        @Path("diaryId") diaryId: Long
    ): Response<ApiResponse<DiaryResponse.Detail>>

    // calendar related
    @GET("/api/v2/diaries")
    suspend fun getList(
        @Query("start") startDate: String, // yyyy-mm-dd
        @Query("end") endDate: String, // yyyy-mm-dd
    ): Response<ApiResponse<DiaryResponse.Diaries>>

    @GET("/api/v2/topics/random")
    suspend fun getTopic(): Response<ApiResponse<DiaryResponse.Topic>>

    @POST("/api/v2/diaries/{diaryId}/corrections")
    suspend fun postCorrection(
        @Path("diaryId") diaryId: Long,
    ): Response<ApiResponse<PostCorrectionResponse>>
}
