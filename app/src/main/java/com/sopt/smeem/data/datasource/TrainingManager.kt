package com.sopt.smeem.data.datasource

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.model.request.PlanRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.TrainingGoalResponse
import com.sopt.smeem.data.service.TrainingService
import com.sopt.smeem.data.service.UserService
import com.sopt.smeem.domain.model.OnBoarding

class TrainingManager(
    private val userService: UserService? = null,
    private val trainingService: TrainingService
) {
    suspend fun patch(onBoarding: OnBoarding): ApiResponse<Unit> =
        userService!!.patchPlan(
            request = PlanRequest(
                target = onBoarding.trainingGoalType,
                trainingTime = onBoarding.extractTime(),
                hasAlarm = onBoarding.hasAlarm
            )
        )

    suspend fun getDetail(goal: TrainingGoalType): ApiResponse<TrainingGoalResponse> =
        trainingService.getDetail(goal)
}