package com.sopt.smeem.presentation.mypage.navigation

import android.widget.Toast
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.sopt.smeem.presentation.mypage.MyPageActivity
import com.sopt.smeem.presentation.mypage.components.topbar.MySummaryTopAppBar
import com.sopt.smeem.presentation.mypage.components.topbar.OnlyBackArrowTopAppBar
import com.sopt.smeem.presentation.mypage.components.topbar.SettingTopAppBar
import com.sopt.smeem.presentation.mypage.components.topbar.TitleTopAppbar
import com.sopt.smeem.presentation.mypage.more.MoreScreen
import com.sopt.smeem.presentation.mypage.mysummary.MySummaryScreen
import com.sopt.smeem.presentation.mypage.mysummary.MySummaryViewModel
import com.sopt.smeem.presentation.mypage.setting.SettingScreen
import com.sopt.smeem.presentation.mypage.setting.nickname.ChangeNicknameScreen
import com.sopt.smeem.presentation.mypage.setting.plan.EditTrainingPlanScreen
import com.sopt.smeem.presentation.mypage.setting.time.EditTrainingTimeScreen

@Composable
fun MyPageNavHost(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val mySummaryViewModel: MySummaryViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            when (currentRoute) {
                MyPageScreen.MySummary.route -> MySummaryTopAppBar(
                    onNavigationIconClick = { if (context is MyPageActivity) context.finish() },
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MyPageScreen.MySummary.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 10,
                        easing = LinearOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 10,
                        easing = LinearOutSlowInEasing
                    )
                )
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 10,
                        easing = LinearOutSlowInEasing
                    )
                )
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 10,
                        easing = LinearOutSlowInEasing
                    )
                )
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            addMySummary(navController = navController, mySummaryViewModel = mySummaryViewModel)
            addSetting(navController = navController, mySummaryViewModel = mySummaryViewModel)
            addMore(navController = navController)
        }
    }
}

private fun NavGraphBuilder.addMySummary(
    navController: NavController,
    mySummaryViewModel: MySummaryViewModel
) {
    composable(route = MyPageScreen.MySummary.route) {

        MySummaryScreen(
            navController = navController,
            viewModel = mySummaryViewModel
        )
    }
}

private fun NavGraphBuilder.addSetting(
    navController: NavController,
    mySummaryViewModel: MySummaryViewModel,
) {
    navigation(
        startDestination = SettingNavGraph.SettingMain.route,
        route = MyPageScreen.Setting.route
    ) {
        composable(route = SettingNavGraph.SettingMain.route) {
            SettingScreen(
                navController = navController,
            )
        }

        composable(
            route = SettingNavGraph.ChangeNickname.route,
            arguments = listOf(navArgument("nickname") { type = NavType.StringType })
        ) { backStackEntry ->
            val nickname = backStackEntry.arguments?.getString("nickname") ?: ""

            ChangeNicknameScreen(
                navController = navController,
                nickname = nickname
            )
        }

        composable(
            route = SettingNavGraph.EditTrainingPlan.route
        ) {
            val context = LocalContext.current

            EditTrainingPlanScreen(
                onEditSuccess = {
                    mySummaryViewModel.setDataChanged(true)
                    navController.navigate(SettingNavGraph.SettingMain.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = false
                        }
                    }

                    Toast.makeText(
                        context,
                        context.getString(R.string.my_page_edit_done_message),
                        Toast.LENGTH_SHORT
                    ).show()
                },
            )
        }

        composable(
            route = SettingNavGraph.EditTrainingTime.route,
        ) {

            EditTrainingTimeScreen(
                navController = navController,
            )
        }
    }
}

private fun NavGraphBuilder.addMore(navController: NavController) {
    composable(route = MyPageScreen.More.route) {
        MoreScreen(
            navController = navController,
        )
    }
}

