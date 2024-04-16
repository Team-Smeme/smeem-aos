package com.sopt.smeem.presentation.mypage.mysummary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.domain.model.mypage.MySmeem
import com.sopt.smeem.presentation.mypage.components.MySmeem

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

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MySmeem(mySmeem = mockMySmeem)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MySummaryScreenPreview() {
    MySummaryScreen(navController = rememberNavController(), modifier = Modifier)
}