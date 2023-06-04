package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.LoginRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    @POST("/api/v2/auth")
    suspend fun login(
        @Header(value = "Authorization") accessToken: String,
        @Body request: LoginRequest
    ): ApiResponse<LoginResponse>
}