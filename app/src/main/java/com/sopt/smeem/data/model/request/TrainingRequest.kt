package com.sopt.smeem.data.model.request

import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.domain.model.Day

data class TrainingRequest(
    val target: TrainingGoalType?,
    val trainingTime: TrainingTime?,
    val hasAlarm: Boolean?
) {
    data class TrainingTime(
        val day: String,
        val hour: Int?,
        val minute: Int?
    ) {
        companion object{
            fun of(day: Collection<Day>, hour: Int?, minute: Int?) =
                TrainingTime(
                    day = with(day) {
                        var daysString = ""
                        this.forEach { day -> daysString += "${day.name}," }
                        daysString.dropLast(1)
                    },
                    hour = hour?.let { if(hour == 0) 22 else hour } ?: 22,
                    minute = minute ?: 0
                )
        }
    }
}