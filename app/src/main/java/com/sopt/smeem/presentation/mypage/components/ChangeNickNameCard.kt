package com.sopt.smeem.presentation.mypage.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sopt.smeem.R

@Composable
fun ChangeNicknameCard(
    nickname: String,
    onEditClick: () -> Unit,
) {
    SmeemContents(title = stringResource(R.string.change_nickname_card)) {
        SmeemCard(
            text = nickname,
        ) {
            EditButton {
                onEditClick()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangeNickNameCardPreview() {
    ChangeNicknameCard(
        nickname = "닉네임",
        onEditClick = {}
    )
}