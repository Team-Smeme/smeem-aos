package com.sopt.smeem.presentation.mypage.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sopt.smeem.presentation.mypage.mysummary.MySummaryScreen
import com.sopt.smeem.presentation.mypage.setting.SettingScreen

@Composable
fun MyPageNavHost(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            // TopAppBar
        }
    ) {
        NavHost(navController = navController, startDestination = MyPageScreen.MySummary.route) {
            addMySummary(navController = navController, modifier = Modifier.padding(it))
            addSetting(navController = navController, modifier = Modifier.padding(it))
        }
    }
}

private fun NavGraphBuilder.addMySummary(navController: NavController, modifier: Modifier) {
    composable(route = MyPageScreen.MySummary.route) {
        MySummaryScreen(
            navController = navController,
            modifier = modifier
        )
    }
}

private fun NavGraphBuilder.addSetting(navController: NavController, modifier: Modifier) {
    composable(route = MyPageScreen.Setting.route) {
        SettingScreen(
            navController = navController,
            modifier = modifier
        )
    }
}