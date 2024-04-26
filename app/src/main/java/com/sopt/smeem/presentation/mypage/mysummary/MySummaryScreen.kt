package com.sopt.smeem.presentation.mypage.mysummary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.domain.model.mypage.MyPlan
import com.sopt.smeem.domain.model.mypage.MySmeem
import com.sopt.smeem.presentation.mypage.components.MyPlanCard
import com.sopt.smeem.presentation.mypage.components.MySmeemCard
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun MySummaryScreen(
    navController: NavController,
    modifier: Modifier
) {

    val mockMySmeem = MySmeem(
        visitDays = 23,
        diaryCount = 19,
        diaryComboCount = 3,
        badgeCount = 10
    )

    val mockMyPlan = MyPlan(
        plan = "매일 일기 작성하기",
        goal = "유창한 비즈니스 영어",
        clearedCount = 2,
        clearCount = 7
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VerticalSpacer(height = 18.dp)

        MySmeemCard(mySmeem = mockMySmeem)

        VerticalSpacer(height = 44.dp)

        MyPlanCard(myPlan = mockMyPlan)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MySummaryScreenPreview() {
    MySummaryScreen(navController = rememberNavController(), modifier = Modifier)
}