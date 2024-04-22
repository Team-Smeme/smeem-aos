package com.sopt.smeem.presentation.mypage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.presentation.compose.theme.SmeemTheme
import com.sopt.smeem.presentation.mypage.navigation.MyPageNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TempMyPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmeemTheme {
                val navController = rememberNavController()
                MyPageNavHost(navController = navController)
            }
        }
    }
}