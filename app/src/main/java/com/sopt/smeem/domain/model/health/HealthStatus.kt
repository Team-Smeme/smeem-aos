package com.sopt.smeem.domain.model.health

import com.sopt.smeem.data.model.response.ApiResponse

data class HealthStatus(
    var status: Int,
    val error: String?
) {
    fun statusPostFix() = "status : $status"
    fun messagePostFix() = if (error.isNullOrEmpty()) "" else "message : $error"

    companion object {
        fun init(response: ApiResponse<Unit>) = HealthStatus(status = response.status, error = response.message)
    }
}