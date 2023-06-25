package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.response.ApiResponse
import retrofit2.http.GET

interface HealthService {
    @GET("/api/v2/test")
    suspend fun getStatus(): ApiResponse<Unit>
}