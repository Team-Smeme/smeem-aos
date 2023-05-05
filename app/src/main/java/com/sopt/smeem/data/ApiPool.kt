package com.sopt.smeem.data

import com.sopt.smeem.data.service.HealthService

object ApiPool {
    val healthService: HealthService = PreparedRetrofit.apiServerRetrofitForAnonymous.create(HealthService::class.java)
}
