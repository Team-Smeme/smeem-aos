package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.PushRequest
import com.sopt.smeem.data.model.request.TrainingRequest
import com.sopt.smeem.data.model.request.UserInfoModifyingRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.MyInfoResponse
import com.sopt.smeem.data.model.response.MyPlanDataResponse
import com.sopt.smeem.data.model.response.MySmeemDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface UserService {
    @PATCH("/api/v2/members/plan")
    suspend fun patchPlan(
        @Body request: TrainingRequest
    ): Response<ApiResponse<Unit>>

    @PATCH("/api/v2/members/plan")
    suspend fun patchPlanWithFixedToken(
        @Header("Authorization") token: String,
        @Body request: TrainingRequest,
    ): Response<ApiResponse<Unit>>

    @PATCH("/api/v2/members/plan")
    suspend fun patchPlanOnAnonymous(
        @Body request: TrainingRequest,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>

    @PATCH("/api/v2/members")
    suspend fun patchUserInfo(
        @Body request: UserInfoModifyingRequest
    ): Response<ApiResponse<Unit>>

    @PATCH("/api/v2/members")
    suspend fun patchUserInfoWithTokenFixed(
        @Header("Authorization") token: String,
        @Body request: UserInfoModifyingRequest,
    ): Response<ApiResponse<Unit>>

    @PATCH("/api/v2/members/push")
    suspend fun patchPush(
        @Body request: PushRequest
    ): Response<ApiResponse<Unit>>

    @PATCH("/api/v2/members/push")
    suspend fun patchPushWithFixedToken(
        @Header("Authorization") token: String,
        @Body request: PushRequest
    ): Response<ApiResponse<Unit>>

    @DELETE("/api/v2/auth")
    suspend fun delete(): Response<ApiResponse<Unit>>

    @GET("/api/v2/members/performance/summary")
    suspend fun getMySmeemData(): Response<ApiResponse<MySmeemDataResponse>>

    @GET("/api/v2/members/plan") // data 가 nullable 합니다.
    suspend fun getMyPlanData(): Response<ApiResponse<MyPlanDataResponse>>

    @GET("/api/v2/members/me")
    suspend fun getMyInfo(): Response<ApiResponse<MyInfoResponse>>

    @PATCH("/api/v2/members/visit")
    suspend fun visit(): Response<ApiResponse<Unit>>
}