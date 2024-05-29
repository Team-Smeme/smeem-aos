package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.VersionResponse
import retrofit2.Response
import retrofit2.http.GET

interface VersionService {
    @GET("/api/v2/versions/client/app")
    suspend fun checkVersion(): Response<ApiResponse<VersionResponse>>
}