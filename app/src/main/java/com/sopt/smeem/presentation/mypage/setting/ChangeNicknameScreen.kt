package com.sopt.smeem.presentation.mypage.setting

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChangeNicknameScreen(
    modifier: Modifier = Modifier,
    nickname: String
) {
    Text(text = "ChangeNicknameScreen: $nickname", modifier = modifier)

}