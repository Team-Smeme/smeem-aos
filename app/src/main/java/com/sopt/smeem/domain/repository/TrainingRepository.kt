package com.sopt.smeem.domain.repository

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.domain.ApiResult
import com.sopt.smeem.domain.dto.TrainingGoalDto
import com.sopt.smeem.domain.dto.TrainingGoalSimpleDto
import com.sopt.smeem.domain.dto.TrainingPlanDto

interface TrainingRepository {
    suspend fun getDetail(goal: TrainingGoalType): ApiResult<TrainingGoalDto>

    suspend fun getAll(): ApiResult<TrainingGoalSimpleDto>

    suspend fun getPlans(): ApiResult<List<TrainingPlanDto>>
}