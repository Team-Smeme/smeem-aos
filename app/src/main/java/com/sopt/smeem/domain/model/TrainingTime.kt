package com.sopt.smeem.domain.model

import com.sopt.smeem.data.model.response.MyPageResponse
import com.sopt.smeem.util.DateUtil.asAmpm
import com.sopt.smeem.util.DateUtil.asHour
import com.sopt.smeem.util.DateUtil.asMinute
import kotlin.math.min

data class TrainingTime(
    val days: Set<Day>,
    val hour: Int,
    val minute: Int
) {
    fun isSet() = days.isNotEmpty()

    fun asText() = "${"%02d".format(asHour(hour))}:${asMinute(minute)} ${asAmpm(hour)}"

    companion object {
        fun from(trainingResponse: MyPageResponse.TrainingResponse) = TrainingTime(
            // TODO: BindingAdapter로 응용 가능할듯?
            days = trainingResponse.day.split(",").map { Day.valueOf(it) }.toSet(),
            hour = asHour(trainingResponse.hour),
            minute = trainingResponse.minute
        )
    }
}