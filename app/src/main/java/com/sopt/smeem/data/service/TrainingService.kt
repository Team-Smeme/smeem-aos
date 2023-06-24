package com.sopt.smeem.data.service

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.TrainingGoalResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrainingService {
    // TODO : server api 추가 필요
    @GET("/api/v2/training")
    suspend fun getDetail(
        @Query("type") goal: TrainingGoalType
    ): ApiResponse<TrainingGoalResponse>
}