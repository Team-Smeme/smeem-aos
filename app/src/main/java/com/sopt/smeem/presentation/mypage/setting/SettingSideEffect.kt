package com.sopt.smeem.presentation.mypage.setting

sealed class SettingSideEffect {
    data class ShowToast(val message: String) : SettingSideEffect()
}