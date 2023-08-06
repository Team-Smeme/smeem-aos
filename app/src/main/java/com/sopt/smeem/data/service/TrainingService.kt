package com.sopt.smeem.data.service

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.TrainingGoalResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrainingService {
    @GET("/api/v2/goals/{type}")
    suspend fun getDetail(
        @Path("type") path: TrainingGoalType
    ): ApiResponse<TrainingGoalResponse>
}