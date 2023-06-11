package com.sopt.smeem.data.service

import com.sopt.smeem.StudyGoal
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.GoalDetailResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoalService {
    @GET("/api/v2/goal") // TODO : server spec 에 맞춰
    suspend fun getDetail(
        @Query("goal") studyGoal: StudyGoal
    ): ApiResponse<GoalDetailResponse>
}