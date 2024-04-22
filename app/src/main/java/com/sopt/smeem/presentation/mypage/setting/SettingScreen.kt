package com.sopt.smeem.presentation.mypage.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.presentation.mypage.components.ChangeMyPlanCard
import com.sopt.smeem.presentation.mypage.components.ChangeNicknameCard
import com.sopt.smeem.presentation.mypage.components.TargetLanguageCard
import com.sopt.smeem.presentation.mypage.navigation.MyPageScreen
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun SettingScreen(
    navController: NavController,
    modifier: Modifier
) {
    val mockNickname = "이태하이"
    val mockMyPlan = "주 3회 일기 작성하기"

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VerticalSpacer(height = 50.dp)

        ChangeNicknameCard(
            nickname = mockNickname,
            onEditClick = {
                navController.navigate(MyPageScreen.ChangeNickname.createRoute(mockNickname))
            }
        )

        VerticalSpacer(height = 28.dp)

        ChangeMyPlanCard(myPlan = mockMyPlan, onEditClick = {})

        VerticalSpacer(height = 28.dp)

        TargetLanguageCard(onEditClick = {})
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SettingScreenPreview() {
    SettingScreen(navController = rememberNavController(), modifier = Modifier)
}
