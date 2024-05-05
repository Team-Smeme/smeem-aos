package com.sopt.smeem.data.model.response

data class MyInfoResponse(
    val username: String,
    val way: String,
    val detail: String,
    val targetLang: String,
    val hasPushAlarm: Boolean,
    val trainingTime: MyTrainingTimeResponse?,
    val trainingPlan: MyTrainingPlanResponse?
) {
    data class MyTrainingTimeResponse(
        val day: String,
        val hour: Int,
        val minute: Int
    )

    data class MyTrainingPlanResponse(
        val id: Int,
        val content: String
    )
}