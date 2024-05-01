package com.sopt.smeem.data.service

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.TrainingGoalResponse
import com.sopt.smeem.data.model.response.TrainingGoalSimpleResponse
import com.sopt.smeem.data.model.response.TrainingPlansResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TrainingService {
    @GET("/api/v2/goals/{type}")
    suspend fun getDetail(
        @Path("type") path: TrainingGoalType
    ): Response<ApiResponse<TrainingGoalResponse>>

    @GET("/api/v2/goals")
    suspend fun getAll(): Response<ApiResponse<TrainingGoalSimpleResponse>>

    @GET("/api/v2/plan")
    suspend fun getPlans(): Response<ApiResponse<TrainingPlansResponse>>
}