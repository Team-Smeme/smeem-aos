package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.PlanRequest
import com.sopt.smeem.data.model.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.PATCH

interface UserService {
    @PATCH("/api/v2/members/plan")
    suspend fun patchPlan(
        @Body request: PlanRequest
    ) : ApiResponse<Unit>
}