package com.sopt.smeem.domain.model

import com.sopt.smeem.data.model.response.MyPageResponse
import com.sopt.smeem.util.DateUtil.asAmpm
import com.sopt.smeem.util.DateUtil.asHour

data class AlarmEnabledDay(
    val days: Set<Day>,
    val hour: Int,
    val minute: Int,
    val ampm: String
) {
    fun asText() = "${hour}:${minute} ${ampm}"

    companion object {
        fun from(trainingResponse: MyPageResponse.TrainingResponse) = AlarmEnabledDay(
            days = trainingResponse.day,
            hour = asHour(trainingResponse.hour.toInt()),
            minute = trainingResponse.minute.toInt(),
            ampm = asAmpm(trainingResponse.hour.toInt())
        )
    }
}
