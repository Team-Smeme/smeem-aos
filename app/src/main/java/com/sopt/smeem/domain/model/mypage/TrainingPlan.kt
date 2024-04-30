package com.sopt.smeem.domain.model.mypage

data class TrainingPlan(
    val id: Int,
    val content: String
)

enum class TrainingPlanDescription(val id: Int, val description: String) {
    ONCE_A_WEEK(1, "주 1회 작성할게요"),
    TWICE_A_WEEK(2, "주 3회 작성할게요"),
    THREE_TIMES_A_WEEK(3, "주 5회 작성할게요"),
    DAILY(4, "매일 작성할게요")
}
