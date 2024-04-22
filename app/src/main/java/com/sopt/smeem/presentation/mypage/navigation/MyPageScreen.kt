package com.sopt.smeem.presentation.mypage.navigation

import com.sopt.smeem.presentation.mypage.CHANGE_NICKNAME
import com.sopt.smeem.presentation.mypage.MORE
import com.sopt.smeem.presentation.mypage.MY_SUMMARY
import com.sopt.smeem.presentation.mypage.SETTING

sealed class MyPageScreen(val route: String) {
    data object MySummary : MyPageScreen(MY_SUMMARY)
    data object Setting : MyPageScreen(SETTING)
    data object More : MyPageScreen(MORE)

    data object ChangeNickname : MyPageScreen("$CHANGE_NICKNAME/{nickname}") {
        fun createRoute(nickname: String): String = "$CHANGE_NICKNAME/$nickname"
    }
}