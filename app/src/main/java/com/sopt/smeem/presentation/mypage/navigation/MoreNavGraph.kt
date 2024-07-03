package com.sopt.smeem.presentation.mypage.navigation

import com.sopt.smeem.presentation.mypage.DELETE_ACCOUNT
import com.sopt.smeem.presentation.mypage.MORE_MAIN

sealed class MoreNavGraph(val route: String) {
    data object MoreMain : MoreNavGraph(MORE_MAIN)

    data object DeleteAccount : SettingNavGraph(DELETE_ACCOUNT)
}