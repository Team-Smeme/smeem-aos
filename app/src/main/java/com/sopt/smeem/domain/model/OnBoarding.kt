package com.sopt.smeem.domain.model

import com.sopt.smeem.Day
import com.sopt.smeem.StudyGoal
import com.sopt.smeem.data.model.request.PlanRequest

data class OnBoarding(
    val studyGoal: StudyGoal,
    val hasAlarm: Boolean,
    val day: List<Day> = emptyList(),
    val hour: Int?,
    val minute: Int?
) {
    fun extractTime(): PlanRequest.TrainingTime? {
        if (day.isEmpty()) {
            return null
        }

        return PlanRequest.TrainingTime(
            day = day,
            hour = requireNotNull(hour).toString(),
            minute = requireNotNull(minute).toString()
        )
    }
}