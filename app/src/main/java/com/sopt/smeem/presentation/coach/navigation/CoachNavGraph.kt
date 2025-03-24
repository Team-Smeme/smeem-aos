package com.sopt.smeem.presentation.coach.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.presentation.coach.CoachDetailRoute
import com.sopt.smeem.presentation.coach.CoachRoute
import com.sopt.smeem.presentation.coach.CoachSurveyRoute
import com.sopt.smeem.presentation.coach.CoachViewModel

@Composable
fun CoachNavGraph(
    viewModel: CoachViewModel,
    eventVm: EventVM,
    navController: NavHostController,
    onCloseClick: () -> Unit,
) {
    NavHost(navController = navController, startDestination = CoachRoute.Coach) {
        composable<CoachRoute.Coach> {
            CoachRoute(
                viewModel = viewModel,
                eventVm = eventVm,
                navController = navController,
                onCloseClick = onCloseClick
            )
        }
        composable<CoachRoute.CoachDetail> {
            CoachDetailRoute(
                viewModel = viewModel,
                eventVm = eventVm,
                navController = navController,
                navigateToSurvey = { username: String, totalCount: Int ->
                    navController.navigate(CoachRoute.CoachSurvey(username = username, totalCount = totalCount))
                },
                onCloseClick = onCloseClick
            )
        }
        composable<CoachRoute.CoachSurvey> {
            CoachSurveyRoute(
                viewModel = viewModel,
                eventVm = eventVm,
                onCloseClick = onCloseClick
            )
        }
    }
}

fun NavController.navigateToCoachDetail() {
    navigate(CoachRoute.CoachDetail)
}
