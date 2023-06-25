package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.JoinRequest
import com.sopt.smeem.data.model.request.TrainingRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.JoinResponse
import retrofit2.http.Body
import retrofit2.http.PATCH

interface UserService {
    @PATCH("/api/v2/members/plan")
    suspend fun patchPlan(
        @Body request: TrainingRequest
    ): ApiResponse<Unit>

    @PATCH("/api/v2/users")
    suspend fun join(
        @Body request: JoinRequest
    ): ApiResponse<JoinResponse>
}