package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.ApiPool
import com.sopt.smeem.data.model.response.ApiResponse

class HealthChecking {
    suspend fun getHealth(): ApiResponse<Unit> = ApiPool.healthService.getStatus()
}