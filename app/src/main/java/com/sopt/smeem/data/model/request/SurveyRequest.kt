package com.sopt.smeem.data.model.request

import com.sopt.smeem.domain.model.SurveyType

data class SurveyRequest(
    val diaryId: Long,
    val isSatisfied: Boolean,
    val dissatisfactionTypes: List<SurveyType>,
    val reason: String,
)
