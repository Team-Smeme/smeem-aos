package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.service.HealthService

class HealthChecker(
    private val healthService: HealthService
) {
    suspend fun getHealth(): ApiResponse<Unit> = healthService.getStatus()
}