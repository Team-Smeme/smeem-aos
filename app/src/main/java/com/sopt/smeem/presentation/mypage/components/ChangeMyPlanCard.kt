package com.sopt.smeem.presentation.mypage.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sopt.smeem.R

@Composable
fun ChangeMyPlanCard(
    isPlanSet: Boolean = true,
    myPlan: String?,
    onEditClick: () -> Unit,
) {
    SmeemContents(title = stringResource(R.string.my_plan)) {
        SmeemCard(
            text = if (isPlanSet) myPlan!! else stringResource(R.string.no_plan_title),
            isActive = isPlanSet
        ) {
            EditButton(
                text = if (isPlanSet) stringResource(R.string.smeem_card_edit) else stringResource(R.string.set_my_plan)
            ) {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangeNoMyPlanCardPreview() {
    ChangeMyPlanCard(
        isPlanSet = false,
        myPlan = "주 3회 일기 작성하기",
        onEditClick = {}
    )
}