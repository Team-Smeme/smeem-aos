package com.sopt.smeem.domain.dto

import com.sopt.smeem.domain.model.TrainingGoalType

data class TrainingGoalSimpleDto(
    val goalType: TrainingGoalType,
    val name: String
)
