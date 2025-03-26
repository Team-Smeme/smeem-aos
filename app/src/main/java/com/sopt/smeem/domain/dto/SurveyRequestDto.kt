package com.sopt.smeem.domain.dto

import com.sopt.smeem.domain.model.SurveyType

data class SurveyRequestDto(
    val diaryId: Long,
    val isSatisfied: Boolean,
    val dissatisfactionTypes: List<SurveyType>,
    val reason: String,
)
