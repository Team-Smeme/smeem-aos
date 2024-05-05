package com.sopt.smeem.domain.dto

data class MyPlanDtoWrapper(
    val isResponseBodyNull: Boolean,
    val dto: MyPlanDto? = null,
)

data class MyPlanDto(
    val plan: String,
    val goal: String,
    val clearedCount: Int,
    val clearCount: Int,
)