package com.sopt.smeem.domain.model

data class MyPage(
    val username: String,
    val badge: Badge,
    val hasPushAlarm: Boolean,
    val goal: TrainingGoal,
    val language: Language,
    val training: Training
)
