package com.sopt.smeem.domain.model

data class MyPage(
    val username: String,
    val badge: Badge,
    val hasPushAlarm: Boolean,
    val goal: OnBoardingGoal,
    val language: Language,
    val training: Training
)
