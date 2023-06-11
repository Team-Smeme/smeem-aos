package com.sopt.smeem.data.repository

import com.sopt.smeem.data.datasource.HealthChecker
import com.sopt.smeem.domain.model.health.HealthStatus
import com.sopt.smeem.domain.repository.HealthRepository

class HealthRepositoryImpl(
    private val healthChecker: HealthChecker
) : HealthRepository {
    override suspend fun getHealth(): Result<HealthStatus> =
        kotlin.runCatching { healthChecker.getHealth() }.map {
                response -> HealthStatus.init(response)
        }
}