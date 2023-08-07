package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.MyPageResponse
import retrofit2.http.GET

interface MyPageService {
    @GET("/api/v2/members/me")
    suspend fun getMyInfo(): ApiResponse<MyPageResponse>
}