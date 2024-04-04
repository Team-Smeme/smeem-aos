package com.sopt.smeem.presentation.mypage.mysummary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sopt.smeem.presentation.mypage.SETTING
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun MySummaryScreen(
    navController: NavController,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "성과요약 화면이다")
        VerticalSpacer(height = 50.dp)
        Button(onClick = {
            navController.navigate(SETTING) {
                launchSingleTop = true
                restoreState = true
            }
        }) {
            Text(text = "설정 화면으로 이동한다")
        }
    }
}