package com.sopt.smeem.data.datasource

import com.sopt.smeem.SocialType
import com.sopt.smeem.data.model.request.LoginRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.LoginResponse
import com.sopt.smeem.data.service.LoginService

class LoginExecutor(
    private val loginService: LoginService
) {
    suspend fun execute(
        accessToken: String,
        socialType: SocialType
        // refreshToken: String
    ): ApiResponse<LoginResponse> =
        loginService.login(accessToken = "Bearer $accessToken", LoginRequest(socialType))
}