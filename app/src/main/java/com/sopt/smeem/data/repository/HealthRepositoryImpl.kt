package com.sopt.smeem.data.repository

import com.sopt.smeem.data.service.HealthService
import com.sopt.smeem.domain.common.ApiResult
import com.sopt.smeem.domain.repository.HealthRepository

class HealthRepositoryImpl(
    private val healthService: HealthService
) : HealthRepository {
    override suspend fun getHealth(): ApiResult<Unit> =
        healthService.getStatus().let { response ->
            if (response.isSuccessful) {
                ApiResult(response.code(), Unit)
            } else {
                throw response.code().handleStatusCode()
            }
        }
}