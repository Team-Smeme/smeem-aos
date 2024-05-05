package com.sopt.smeem.domain.dto

import com.sopt.smeem.domain.model.TrainingTime

data class MyInfoDto(
    val username: String,
    val way: String,
    val detail: String,
    val targetLang: String,
    val hasPushAlarm: Boolean,
    val trainingTime: TrainingTime?,
    val trainingPlan: MyTrainingPlan?
) {
    data class MyTrainingPlan(
        val id: Int,
        val content: String,
    )
}