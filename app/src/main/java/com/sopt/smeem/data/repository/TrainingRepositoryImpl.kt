package com.sopt.smeem.data.repository

import com.sopt.smeem.data.service.TrainingService
import com.sopt.smeem.domain.common.ApiResult
import com.sopt.smeem.domain.dto.TrainingGoalDto
import com.sopt.smeem.domain.dto.TrainingGoalSimpleDto
import com.sopt.smeem.domain.dto.TrainingPlanDto
import com.sopt.smeem.domain.model.TrainingGoalType
import com.sopt.smeem.domain.repository.TrainingRepository

class TrainingRepositoryImpl(
    private val trainingService: TrainingService,
) : TrainingRepository {
    override suspend fun getDetail(goal: TrainingGoalType): ApiResult<TrainingGoalDto> =
        trainingService.getDetail(goal).let { response ->
            if (response.isSuccessful) {
                response.body()!!.data.let { data ->
                    ApiResult(
                        response.code(), TrainingGoalDto(
                            title = data.title,
                            goal = data.name,
                            way = data.way,
                            detail = data.detail,
                        )
                    )
                }
            } else {
                throw response.code().handleStatusCode()
            }
        }

    override suspend fun getAll(): ApiResult<TrainingGoalSimpleDto> =
        trainingService.getAll().let { response ->
            if (response.isSuccessful) {
                response.body()!!.data.let { data ->
                    ApiResult(
                        response.code(), TrainingGoalSimpleDto(
                            goalType = TrainingGoalType.valueOf(data.goalType),
                            name = data.name,
                        )
                    )
                }
            } else {
                throw response.code().handleStatusCode()
            }
        }

    override suspend fun getPlans(): ApiResult<List<TrainingPlanDto>> =
        trainingService.getPlans().let { response ->
            if (response.isSuccessful) {
                ApiResult(
                    response.code(),
                    response.body()!!.data.plans.map { planResponse ->
                        TrainingPlanDto(
                            id = planResponse.id,
                            content = planResponse.content,
                        )
                    }
                )
            } else {
                throw response.code().handleStatusCode()
            }
        }
}