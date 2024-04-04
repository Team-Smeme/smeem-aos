package com.sopt.smeem.presentation.mypage.setting

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
import com.sopt.smeem.presentation.mypage.MY_SUMMARY
import com.sopt.smeem.presentation.mypage.SETTING
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun SettingScreen(
    navController: NavController,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "설정 화면이다")
        VerticalSpacer(height = 50.dp)
        Button(onClick = {
            navController.navigate(MY_SUMMARY) {
                popUpTo(SETTING) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }) {
            Text(text = "다시 성과 요약 화면으로 이동한다")
        }
    }
}