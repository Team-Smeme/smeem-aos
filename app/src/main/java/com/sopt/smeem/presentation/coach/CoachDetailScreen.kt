package com.sopt.smeem.presentation.coach

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun CoachDetailRoute(
    navController: NavController,
    viewModel: CoachViewModel = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    val state = viewModel.collectAsState().value

    CoachDetailScreen(
        state = state,
        onCloseClick = onCloseClick,
    )
}

@Composable
fun CoachDetailScreen(
    state: CoachState,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "CoachDetailScreen")
    }

}
