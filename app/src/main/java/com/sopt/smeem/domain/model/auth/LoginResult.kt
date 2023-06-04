package com.sopt.smeem.domain.model.auth

import com.sopt.smeem.data.model.response.LoginResponse

data class LoginResult(
    val isRegistered: Boolean,
    val isPlanRegistered: Boolean
) {
    companion object {
        fun from(response: LoginResponse): LoginResult =
            LoginResult(response.isRegistered, response.hasPlan)
    }
}