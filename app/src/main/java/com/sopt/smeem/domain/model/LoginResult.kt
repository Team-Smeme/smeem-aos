package com.sopt.smeem.domain.model

import com.sopt.smeem.data.model.response.LoginResponse

data class LoginResult(
    val apiAccessToken: String,
    val apiRefreshToken: String,
    val isRegistered: Boolean,
    val isPlanRegistered: Boolean
) {
    companion object {
        fun from(response: LoginResponse): LoginResult =
            LoginResult(
                apiAccessToken = response.accessToken,
                apiRefreshToken = response.refreshToken,
                isRegistered = response.isRegistered,
                isPlanRegistered = response.hasPlan
            )
    }
}