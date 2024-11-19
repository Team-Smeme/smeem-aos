package com.sopt.smeem.presentation.coach

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.util.VerticalSpacer
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun CoachDetailRoute(
    navController: NavController,
    viewModel: CoachViewModel = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    val state = viewModel.collectAsState().value

    if (state.isLoading) {
        CoachLoadingScreen()
    } else {
        CoachDetailScreen(
            state = state,
            onCloseClick = onCloseClick,
        )
    }
}

@Composable
fun CoachLoadingScreen(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.smeem_loading))

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier.fillMaxWidth().height(164.dp),
            composition = composition,
        )

        VerticalSpacer(8.dp)

        Text(
            text = "AI 코치가 내 일기를 분석하고 있어요\n잠시만 기다려주세요",
            style = Typography.bodySmall,
            color = black
        )
    }

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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CoachLoadingScreenPreview() {
    CoachLoadingScreen()
}
