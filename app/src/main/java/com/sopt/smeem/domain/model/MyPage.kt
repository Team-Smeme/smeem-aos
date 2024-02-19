package com.sopt.smeem.domain.model

data class MyPage(
    val username: String,
    val myPageBadge: MyPageBadge,
    val hasPushAlarm: Boolean,
    val goal: TrainingGoal,
    val language: Language,
    val trainingTime: TrainingTime
)
