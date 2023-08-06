package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.UserInfoModifyingRequest
import com.sopt.smeem.data.model.request.TrainingRequest
import com.sopt.smeem.data.model.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH

interface UserService {
    @PATCH("/api/v2/members/plan")
    suspend fun patchPlan(
        @Body request: TrainingRequest
    ): ApiResponse<Unit>


    @PATCH("/api/v2/members/plan")
    suspend fun patchPlanOnAnonymous(
        @Body request: TrainingRequest,
        @Header("Authorization") token: String
    ): ApiResponse<Unit>

    @PATCH("/api/v2/members")
    suspend fun patchUserInfo(
        @Body request: UserInfoModifyingRequest
    ): ApiResponse<Unit>
}