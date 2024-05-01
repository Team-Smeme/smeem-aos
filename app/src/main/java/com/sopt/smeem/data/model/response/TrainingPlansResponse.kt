package com.sopt.smeem.data.model.response

data class TrainingPlansResponse(
    val plans: List<TrainingPlanResponse>,
) {
    data class TrainingPlanResponse(
        val id: Long,
        val content: String,
    )
}