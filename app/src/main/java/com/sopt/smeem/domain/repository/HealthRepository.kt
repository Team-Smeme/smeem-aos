package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.health.HealthStatus

interface HealthRepository {
    suspend fun getHealth(): Result<HealthStatus>
}