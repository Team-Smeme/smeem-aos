package com.sopt.smeem.presentation.mypage.mysummary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.data.datasource.BadgeList
import com.sopt.smeem.domain.model.mypage.MyBadges
import com.sopt.smeem.domain.model.mypage.MyPlan
import com.sopt.smeem.domain.model.mypage.MySmeem
import com.sopt.smeem.presentation.mypage.components.MyBadgesBottomSheet
import com.sopt.smeem.presentation.mypage.components.MyBadgesContent
import com.sopt.smeem.presentation.mypage.components.MyPlanCard
import com.sopt.smeem.presentation.mypage.components.MySmeemCard
import com.sopt.smeem.presentation.compose.theme.SmeemTheme
import com.sopt.smeem.util.VerticalSpacer

@OptIn(ExperimentalMaterial3Api::class)
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

    val sheetState = rememberModalBottomSheetState()
    var selectedBadge by rememberSaveable { mutableStateOf<MyBadges?>(null) }

    if (selectedBadge != null) {
        MyBadgesBottomSheet(
            badge = selectedBadge!!,
            sheetState = sheetState,
            onDismiss = { selectedBadge = null }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VerticalSpacer(height = 18.dp)

        MySmeemCard(mySmeem = mockMySmeem)

        VerticalSpacer(height = 44.dp)

        MyPlanCard(myPlan = mockMyPlan)

        VerticalSpacer(height = 36.dp)

        MyBadgesContent(
            badges = BadgeList.sprint2,
            onClickCard = { clickedBadge -> selectedBadge = clickedBadge },
            modifier = Modifier.heightIn(max = 1000.dp)
        )

        VerticalSpacer(height = 120.dp)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MySummaryScreenPreview() {
    SmeemTheme {
        MySummaryScreen(navController = rememberNavController(), modifier = Modifier)
    }
}