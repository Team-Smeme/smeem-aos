package com.sopt.smeem.presentation.mypage.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SendReviewCard(onEditClick: () -> Unit) {
    SmeemContents(title = "의견 보내기") {
        SmeemCard(
            text = "스밈에 대한 의견을 남겨주세요 :)",
        ) {
            EditButton(
                text = "바로가기",
            ) {
                onEditClick()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SendReviewCardPreview() {
    SendReviewCard(
        onEditClick = {},
    )
}
