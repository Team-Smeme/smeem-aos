package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.model.request.PlanRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.service.UserService
import com.sopt.smeem.domain.model.OnBoarding

class PlanSetter(
    private val userService: UserService
) {
    suspend fun patch(onBoarding: OnBoarding): ApiResponse<Unit> =
        userService.patchPlan(
            request = PlanRequest(
                target = onBoarding.studyGoal,
                trainingTime = onBoarding.extractTime(),
                hasAlarm = onBoarding.hasAlarm
            )
        )

    /*    suspend fun getDetail(studyGoal: StudyGoal): ApiResponse<GoalDetailResponse> =
            apiPool.goalServiceWithoutAuth.getDetail(studyGoal = studyGoal)*/
}