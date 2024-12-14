package com.sopt.smeem.presentation.coach

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sopt.smeem.R
import com.sopt.smeem.event.AmplitudeEventType
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.presentation.coach.navigation.navigateToCoachDetail
import com.sopt.smeem.presentation.compose.components.CoachBanner
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray500
import com.sopt.smeem.presentation.compose.theme.white
import com.sopt.smeem.presentation.detail.DiaryDetail
import com.sopt.smeem.util.VerticalSpacer
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CoachRoute(
    navController: NavController,
    viewModel: CoachViewModel = hiltViewModel(),
    eventVm: EventVM = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is CoachSideEffect.NavigateToCoachDetail -> {
                navController.navigateToCoachDetail()
            }
        }
    }

    BackHandler {
        eventVm.sendEvent(
            AmplitudeEventType.COACHING_EXIT_CLICK,
            mapOf("active" to state.isCoachEnabled)
        )
        onCloseClick()
    }

    CoachScreen(
        state = state,
        onCloseClick = {
            eventVm.sendEvent(
                AmplitudeEventType.COACHING_EXIT_CLICK,
                mapOf(AmplitudeEventType.COACHING_EXIT_CLICK.propertyKey.toString() to state.isCoachEnabled)
            )
            onCloseClick()
        },
        onCoachClick = {
            viewModel.onCoachClick()
            eventVm.sendEvent(
                AmplitudeEventType.COACHING_TRY_CLICK,
                mapOf(AmplitudeEventType.COACHING_TRY_CLICK.propertyKey.toString() to state.isCoachEnabled)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachScreen(
    state: CoachState,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onCoachClick: () -> Unit = {},
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
                .background(white)
                .padding(it)
        ) {
            CoachBanner(
                isEnabled = state.isCoachEnabled,
                onClick = onCoachClick
            )

            Text(
                modifier = Modifier.padding(
                    start = 18.dp,
                    end = 18.dp,
                    top = 16.dp,
                    bottom = 24.dp
                ),
                text = state.diaryContent,
                color = black,
                style = Typography.bodyMedium.copy(lineHeight = 22.sp)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = state.createdAt,
                        style = Typography.labelMedium,
                        color = gray500
                    )
                }

                VerticalSpacer(4.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = state.writerUsername,
                        style = Typography.labelMedium,
                        color = gray500
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CoachScreenPreview() {
    CoachScreen(
        state = CoachState(
            diaryId = 1,
            initialDiaryContent = "오늘은 어떤 일이 있었나요?",
            diaryDetail = DiaryDetail(
                diaryId = 1,
                topic = "일상",
                content = "I watched Avatar with my boyfriend at Hongdae CGV. I should have skimmed the previous season - Avatar1.. I really couldn’t get what they were saying and the universe(??). What I was annoyed then was 두팔 didn’t know that as me. I think 두팔 who is my boyfriend should study before wathcing…. but Avatar2 is amazing movie I think. In my personal opinion, the jjin main character of Avatar2 is not Sully, but his son.",
                createdAt = "2023년 3월 27일 4:18 PM",
                writerUsername = "유진이",
                correctionCount = 3,
                correctionMaxCount = 5
            ),
        ),
        onCloseClick = {}
    )
}
