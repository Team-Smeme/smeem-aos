package com.sopt.smeem.domain.common

data class ApiResult<DATA>(
    val statusCode: Int,
    private val data: DATA,
) {
    fun data(): DATA = data ?: throw SmeemException(SmeemErrorCode.NETWORK_LOAD_ERROR)
}