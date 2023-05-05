package com.sopt.smeem.data

import com.sopt.smeem.data.service.HealthService
import com.sopt.smeem.util.network.PreparedRetrofit

object ApiPool {
    val healthService: HealthService = PreparedRetrofit.apiServerRetrofitForAnonymous.create(HealthService::class.java)
}
