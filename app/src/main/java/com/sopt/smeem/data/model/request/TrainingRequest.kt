package com.sopt.smeem.data.model.request

import com.sopt.smeem.Day
import com.sopt.smeem.TrainingGoalType

data class TrainingRequest(
    val target: TrainingGoalType?,
    val trainingTime: TrainingTime?,
    val hasAlarm: Boolean?
) {
    data class TrainingTime(
        val day: List<Day>,
        val hour: Int?,
        val minute: Int?
    )
}