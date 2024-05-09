package com.sopt.smeem.presentation.mypage.mysummary

import com.sopt.smeem.domain.dto.GetBadgeListDto
import com.sopt.smeem.domain.dto.MyPlanDto
import com.sopt.smeem.domain.dto.MySmeemDataDto

sealed class MySummaryUiState {
    data object Idle : MySummaryUiState()
    data object Loading : MySummaryUiState()
    data class Success(
        val smeemData: MySmeemDataDto,
        val planData: MyPlanDto?,
        val badgesData: List<GetBadgeListDto>
    ) : MySummaryUiState()

    data class Error(val message: String) : MySummaryUiState()
}