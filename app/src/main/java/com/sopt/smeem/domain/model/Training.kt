package com.sopt.smeem.domain.model

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.model.request.TrainingRequest

data class Training(
    val type: TrainingGoalType,
    val trainingTime: TrainingTime? = null,
    val hasAlarm: Boolean? = null
) {
    fun extractTime(): TrainingRequest.TrainingTime? {
        if (trainingTime == null) {
            return null
        }

        return TrainingRequest.TrainingTime(
            day = trainingTime.days.toList(),
            hour = trainingTime.hour,
            minute = trainingTime.minute
        )
    }
}
