package com.sopt.smeem.presentation.mypage.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sopt.smeem.R

@Composable
fun ChangeMyPlanCard(
    myPlan: String,
    onEditClick: () -> Unit,
) {
    SmeemContents(title = stringResource(R.string.my_plan)) {
        SmeemCard(
            // TODO : arg로 받아온 값 넣어주기
            text = myPlan,
        ) {
            EditButton {
                onEditClick()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangeMyPlanCardPreview() {
    ChangeMyPlanCard(
        myPlan = "주 3회 일기 작성하기",
        onEditClick = {}
    )
}