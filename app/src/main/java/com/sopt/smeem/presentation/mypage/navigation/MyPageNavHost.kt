package com.sopt.smeem.presentation.mypage.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.sopt.smeem.R
import com.sopt.smeem.presentation.mypage.TempMyPageActivity
import com.sopt.smeem.presentation.mypage.components.topbar.MySummaryTopAppBar
import com.sopt.smeem.presentation.mypage.components.topbar.OnlyBackArrowTopAppBar
import com.sopt.smeem.presentation.mypage.components.topbar.SettingTopAppBar
import com.sopt.smeem.presentation.mypage.components.topbar.TitleTopAppbar
import com.sopt.smeem.presentation.mypage.more.MoreScreen
import com.sopt.smeem.presentation.mypage.mysummary.MySummaryScreen
import com.sopt.smeem.presentation.mypage.mysummary.MySummaryViewModel
import com.sopt.smeem.presentation.mypage.setting.ChangeNicknameScreen
import com.sopt.smeem.presentation.mypage.setting.EditTrainingPlanScreen
import com.sopt.smeem.presentation.mypage.setting.EditTrainingTimeScreen
import com.sopt.smeem.presentation.mypage.setting.SettingScreen

@Composable
fun MyPageNavHost(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    Scaffold(
        topBar = {
            when (currentRoute) {
                MyPageScreen.MySummary.route -> MySummaryTopAppBar(
                    onNavigationIconClick = { if (context is TempMyPageActivity) context.finish() },
                    onSettingClick = {
                        navController.navigate(MyPageScreen.Setting.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                SettingNavGraph.SettingMain.route -> SettingTopAppBar(
                    onNavigationIconClick = {
                        navController.popBackStack()
                    },
                    onMoreClick = {
                        navController.navigate(MyPageScreen.More.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                MyPageScreen.More.route -> OnlyBackArrowTopAppBar(
                    onNavigationIconClick = { navController.popBackStack() }
                )

                SettingNavGraph.ChangeNickname.route -> TitleTopAppbar(
                    onNavigationIconClick = { navController.popBackStack() },
                    title = stringResource(R.string.my_page_change_nickname)
                )

                SettingNavGraph.EditTrainingPlan.route -> TitleTopAppbar(
                    onNavigationIconClick = { navController.popBackStack() },
                    title = stringResource(R.string.edit_training_plan_title)
                )

                SettingNavGraph.EditTrainingTime.route -> TitleTopAppbar(
                    onNavigationIconClick = { navController.popBackStack() },
                    title = stringResource(R.string.edit_traint_time_top_app_bar_title)
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = MyPageScreen.MySummary.route) {
            addMySummary(navController = navController, modifier = Modifier.padding(it))
            addSetting(navController = navController, modifier = Modifier.padding(it))
            addMore(navController = navController, modifier = Modifier.padding(it))
        }
    }
}

private fun NavGraphBuilder.addMySummary(navController: NavController, modifier: Modifier) {
    composable(route = MyPageScreen.MySummary.route) {
        val viewModel: MySummaryViewModel = hiltViewModel()

        MySummaryScreen(
            navController = navController,
            viewModel = viewModel,
            modifier = modifier
        )
    }
}

private fun NavGraphBuilder.addSetting(navController: NavController, modifier: Modifier) {
    navigation(
        startDestination = SettingNavGraph.SettingMain.route,
        route = MyPageScreen.Setting.route
    ) {
        composable(route = SettingNavGraph.SettingMain.route) {

            SettingScreen(
                navController = navController,
                modifier = modifier
            )
        }

        composable(
            route = SettingNavGraph.ChangeNickname.route,
            arguments = listOf(navArgument("nickname") { type = NavType.StringType })
        ) { backStackEntry ->
            val nickname = backStackEntry.arguments?.getString("nickname") ?: ""

            ChangeNicknameScreen(
                modifier = modifier,
                nickname = nickname
            )
        }

        composable(
            route = SettingNavGraph.EditTrainingPlan.route
        ) {
            EditTrainingPlanScreen(modifier = modifier)
        }

        composable(
            route = SettingNavGraph.EditTrainingTime.route,
        ) {

            EditTrainingTimeScreen(
                navController = navController,
                modifier = modifier,
            )
        }
    }
}

private fun NavGraphBuilder.addMore(navController: NavController, modifier: Modifier) {
    composable(route = MyPageScreen.More.route) {
        MoreScreen(
            navController = navController,
            modifier = modifier
        )
    }
}

