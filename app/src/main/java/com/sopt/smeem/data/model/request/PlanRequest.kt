package com.sopt.smeem.data.model.request

import com.sopt.smeem.Day
import com.sopt.smeem.StudyGoal

data class PlanRequest(
    val target: StudyGoal,
    val trainingTime: TrainingTime?,
    val hasAlarm: Boolean
) {
    data class TrainingTime(
        val day: List<Day>,
        val hour: String?,
        val minute: String?
    )
}