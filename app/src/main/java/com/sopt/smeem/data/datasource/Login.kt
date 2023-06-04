package com.sopt.smeem.data.datasource

import com.sopt.smeem.SocialType
import com.sopt.smeem.data.ApiPool
import com.sopt.smeem.data.model.request.LoginRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.LoginResponse

class Login {
    suspend fun execute(
        accessToken: String,
        socialType: SocialType
        // refreshToken: String
    ): ApiResponse<LoginResponse> =
        ApiPool.loginService.login(accessToken = "Bearer $accessToken", LoginRequest(socialType))
}