package com.sopt.smeem.domain.repository

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.domain.model.TrainingGoal

interface TrainingRepository {
    suspend fun getDetail(goal: TrainingGoalType?): Result<TrainingGoal>
}