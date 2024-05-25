package com.sopt.smeem.domain.dto

import com.sopt.smeem.data.model.request.TrainingRequest
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.TrainingGoalType

data class PostOnBoardingDto(
    val trainingGoalType: TrainingGoalType,
    val planId: Int,
    val hasAlarm: Boolean,
    val day: List<Day> = emptyList(),
    val hour: Int?,
    val minute: Int?,
) {
    fun extractTime(): TrainingRequest.TrainingTime? {
        if (day.isEmpty()) {
            return null
        }

        return TrainingRequest.TrainingTime.of(
            day = day,
            hour = hour,
            minute = minute
        )
    }
}