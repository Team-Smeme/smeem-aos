package com.sopt.smeem.data.datasource

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.model.request.PushRequest
import com.sopt.smeem.data.model.request.TrainingRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.TrainingGoalResponse
import com.sopt.smeem.data.model.response.TrainingGoalSimpleResponse
import com.sopt.smeem.data.service.TrainingService
import com.sopt.smeem.data.service.UserService
import com.sopt.smeem.domain.model.LoginResult
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.PushAlarm
import com.sopt.smeem.domain.model.Training

class TrainingManager(
    private val userService: UserService? = null,
    private val trainingService: TrainingService
) {
    suspend fun registerOnBoarding(
        onBoarding: OnBoarding,
        loginResult: LoginResult
    ): ApiResponse<Unit> {
        return userService!!.patchPlanOnAnonymous(
            request = TrainingRequest(
                target = onBoarding.trainingGoalType,
                trainingTime = onBoarding.extractTime(),
                hasAlarm = onBoarding.hasAlarm
            ),
            token = "Bearer ${loginResult.apiAccessToken}"
        )
    }

    suspend fun patchTraining(accessToken: String?, training: Training): ApiResponse<Unit> {
        if (accessToken.isNullOrBlank()) {
            return userService!!.patchPlan(
                request = TrainingRequest(
                    target = training.type,
                    trainingTime = training.extractTime(),
                    hasAlarm = training.hasAlarm
                )
            )
        } else {
            return userService!!.patchPlanWithFixedToken(
                token = "Bearer $accessToken",
                request = TrainingRequest(
                    target = training.type,
                    trainingTime = training.extractTime(),
                    hasAlarm = training.hasAlarm
                )
            )
        }
    }

    suspend fun patchPushAlarm(accessToken: String?, push: PushAlarm): ApiResponse<Unit> {
        if (accessToken.isNullOrBlank()) {
            return userService!!.patchPush(
                request = PushRequest(push.hasAlarm)
            )
        } else {
            return userService!!.patchPushWithFixedToken(
                token = "Bearer $accessToken",
                request = PushRequest(push.hasAlarm)
            )
        }
    }


    suspend fun getDetail(goal: TrainingGoalType): ApiResponse<TrainingGoalResponse> =
        trainingService.getDetail(goal)

    suspend fun getAll(): ApiResponse<TrainingGoalSimpleResponse> =
        trainingService.getAll()
}