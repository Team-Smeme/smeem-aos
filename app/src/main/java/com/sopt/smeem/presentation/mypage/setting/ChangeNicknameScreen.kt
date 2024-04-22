package com.sopt.smeem.presentation.mypage.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.presentation.compose.components.SmeemTextField
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun ChangeNicknameScreen(
    modifier: Modifier = Modifier,
    nickname: String
) {
    var newNickName by rememberSaveable { mutableStateOf(nickname) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        VerticalSpacer(height = 14.dp)

        SmeemTextField(
            text = newNickName,
            onValueChange = { newNickName = it },
            modifier = Modifier.padding(horizontal = 18.dp)
        )

    }

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ChangeNicknameScreenPreview() {
    ChangeNicknameScreen(nickname = "이태하이")
}