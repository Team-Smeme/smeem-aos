package com.sopt.smeem.data

import com.sopt.smeem.SmeemErrorCode
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.service.HealthService
import retrofit2.HttpException

object ApiPool {
    val healthService: HealthService =
        PreparedRetrofit.apiServerRetrofitForAnonymous.create(HealthService::class.java)
}

fun <T> Result<T>.onHttpFailure(t: HttpException, action: (Throwable) -> Unit) {
    try {
        when (t.code()) {
            400 -> throw SmeemException(SmeemErrorCode.SYSTEM_ERROR)
            401 -> throw SmeemException(SmeemErrorCode.UNAUTHORIZED)
            403 -> throw SmeemException(SmeemErrorCode.FORBIDDEN)
            else -> throw SmeemException(SmeemErrorCode.NETWORK_ERROR)
        }
    } catch (e: SmeemException) {
        action(e)
    }
}