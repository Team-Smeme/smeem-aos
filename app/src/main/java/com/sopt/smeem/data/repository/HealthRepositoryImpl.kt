package com.sopt.smeem.data.repository

import com.sopt.smeem.data.datasource.HealthChecking
import com.sopt.smeem.domain.model.health.HealthStatus
import com.sopt.smeem.domain.repository.HealthRepository

class HealthRepositoryImpl(
    val healthChecking: HealthChecking
) : HealthRepository {
    override suspend fun getHealth(): Result<HealthStatus> =
        kotlin.runCatching { healthChecking.getHealth() }.map {
                response -> HealthStatus.init(response)
        }
}