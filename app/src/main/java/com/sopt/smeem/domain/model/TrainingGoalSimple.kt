package com.sopt.smeem.domain.model

import com.sopt.smeem.TrainingGoalType

data class TrainingGoalSimple(
    val goalType: TrainingGoalType,
    val name: String
)
