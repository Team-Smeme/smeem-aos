package com.sopt.smeem.domain.repository

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.domain.model.TrainingGoal
import com.sopt.smeem.domain.model.TrainingGoalSimple

interface TrainingRepository {
    suspend fun getDetail(goal: TrainingGoalType?): Result<TrainingGoal>

    suspend fun getAll(): Result<TrainingGoalSimple>
}