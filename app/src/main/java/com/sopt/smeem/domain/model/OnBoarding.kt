package com.sopt.smeem.domain.model

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.model.request.TrainingRequest

data class OnBoarding(
    val trainingGoalType: TrainingGoalType,
    val hasAlarm: Boolean,
    val day: List<Day> = emptyList(),
    val hour: Int?,
    val minute: Int?
) {
    fun extractTime(): TrainingRequest.TrainingTime? {
        if (day.isEmpty()) {
            return null
        }

        return TrainingRequest.TrainingTime(
            day = day,
            hour = requireNotNull(hour),
            minute = requireNotNull(minute)
        )
    }
}