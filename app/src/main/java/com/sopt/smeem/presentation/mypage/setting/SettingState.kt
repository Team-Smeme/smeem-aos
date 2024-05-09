package com.sopt.smeem.presentation.mypage.setting

import com.sopt.smeem.domain.dto.MyInfoDto
import com.sopt.smeem.util.UiState

data class SettingState(
    val uiState: UiState<MyInfoDto> = UiState.Idle,
)