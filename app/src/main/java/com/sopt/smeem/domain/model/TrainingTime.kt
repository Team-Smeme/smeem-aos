package com.sopt.smeem.domain.model

data class TrainingTime(
    val days: Set<Day>,
    val hour: Int,
    val minute: Int
) {
    fun isSet() = days.isNotEmpty()
}