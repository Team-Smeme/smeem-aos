package com.sopt.smeem.presentation.mypage.navigation

import com.sopt.smeem.presentation.mypage.CHANGE_NICKNAME
import com.sopt.smeem.presentation.mypage.SETTING_MAIN

sealed class SettingNavGraph(val route: String) {
    data object SettingMain : SettingNavGraph(SETTING_MAIN)

    data object ChangeNickname : SettingNavGraph("$CHANGE_NICKNAME/{nickname}") {
        fun createRoute(nickname: String): String = "$CHANGE_NICKNAME/$nickname"
    }
}