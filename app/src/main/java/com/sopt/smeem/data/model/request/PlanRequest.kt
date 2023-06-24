package com.sopt.smeem.data.model.request

import com.sopt.smeem.Day
import com.sopt.smeem.TrainingGoalType

data class PlanRequest(
    val target: TrainingGoalType,
    val trainingTime: TrainingTime?,
    val hasAlarm: Boolean
) {
    data class TrainingTime(
        val day: List<Day>,
        val hour: String?,
        val minute: String?
    )
}