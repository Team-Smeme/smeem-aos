package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.LoginRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.LoginResponse
import com.sopt.smeem.data.model.response.NicknameCheckResponse
import com.sopt.smeem.data.model.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {
    @POST("/api/v2/auth")
    suspend fun login(
        @Header(value = "Authorization") accessToken: String,
        @Body request: LoginRequest
    ): Response<ApiResponse<LoginResponse>>

    @GET("/api/v2/members/nickname/check")
    suspend fun checkDuplicated(
        @Query("name") nickname: String
    ): Response<ApiResponse<NicknameCheckResponse>>

    @POST("/api/v2/auth/token")
    suspend fun getToken(
        @Header(value = "Authorization") refreshToken: String
    ): Response<ApiResponse<TokenResponse>>
}