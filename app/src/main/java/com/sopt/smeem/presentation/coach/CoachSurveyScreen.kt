package com.sopt.smeem.presentation.coach

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.smeem.R
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.white
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun CoachSurveyRoute(
    viewModel: CoachViewModel = hiltViewModel(),
    eventVm: EventVM = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    val state by viewModel.collectAsState()

    BackHandler {
        onCloseClick()
    }

    CoachSurveyScreen(
        state = state,
        onCloseClick = onCloseClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachSurveyScreen(
    state: CoachState,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
                    containerColor = white
                ),
                title = {},
                navigationIcon = {},
                actions = {
                    Text(
                        text = stringResource(R.string.coach_topappbar_action_text),
                        style = Typography.bodyMedium,
                        color = black,
                        modifier = Modifier
                            .padding(end = 18.dp)
                            .clickable { onCloseClick() }
                    )
                }
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${state.username}님,\n오늘까지 코칭을 ${state.totalCount}번 받으셨네요!\n꾸준함이 대단해요! \uD83D\uDC4F",
                color = black,
                style = Typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCoachSurveyScreen() {
    CoachSurveyScreen(
        state = CoachState(),
        onCloseClick = {}
    )
}
