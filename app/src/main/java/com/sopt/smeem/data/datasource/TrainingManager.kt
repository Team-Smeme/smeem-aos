package com.sopt.smeem.data.datasource

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.model.request.TrainingRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.TrainingGoalResponse
import com.sopt.smeem.data.service.TrainingService
import com.sopt.smeem.data.service.UserService
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.Training

class TrainingManager(
    private val userService: UserService? = null,
    private val trainingService: TrainingService
) {
    suspend fun registerOnBoarding(onBoarding: OnBoarding): ApiResponse<Unit> =
        userService!!.patchPlan(
            request = TrainingRequest(
                target = onBoarding.trainingGoalType,
                trainingTime = onBoarding.extractTime(),
                hasAlarm = onBoarding.hasAlarm
            )
        )

    suspend fun patchTraining(training: Training): ApiResponse<Unit> =
        userService!!.patchPlan(
            request = TrainingRequest(
                target = training.type,
                trainingTime = training.extractTime(),
                hasAlarm = training.hasAlarm
            )
        )

    suspend fun getDetail(goal: TrainingGoalType): ApiResponse<TrainingGoalResponse> =
        trainingService.getDetail(goal)
}