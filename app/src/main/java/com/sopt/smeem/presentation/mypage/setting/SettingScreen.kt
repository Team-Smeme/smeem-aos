package com.sopt.smeem.presentation.mypage.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.domain.dto.MyInfoDto
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.presentation.compose.components.LoadingScreen
import com.sopt.smeem.presentation.compose.components.SmeemAlarmCard
import com.sopt.smeem.presentation.mypage.components.ChangeMyPlanCard
import com.sopt.smeem.presentation.mypage.components.ChangeNicknameCard
import com.sopt.smeem.presentation.mypage.components.StudyNotificationCard
import com.sopt.smeem.presentation.mypage.components.TargetLanguageCard
import com.sopt.smeem.presentation.mypage.navigation.SettingNavGraph
import com.sopt.smeem.util.UiState
import com.sopt.smeem.util.VerticalSpacer
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel,
    modifier: Modifier
) {
    val state by viewModel.collectAsState()
    var isSwitchChecked by rememberSaveable { mutableStateOf(true) }

    when (val uiState = state.uiState) {
        is UiState.Loading -> {
            LoadingScreen()
        }

        is UiState.Success -> {
            val response = uiState.data
            val username = response.username
            val myPlan = response.trainingPlan?.content
            val targetLanguage = response.targetLang
            isSwitchChecked = response.hasPushAlarm
            val selectedTrainingTime = if (response.trainingTime!!.day.isNotEmpty()) {
                response.trainingTime
            } else {
                MyInfoDto.MyTrainingTime(
                    setOf(Day.MON, Day.TUE, Day.WED, Day.THU, Day.FRI),
                    22,
                    0
                )
            }

            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                VerticalSpacer(height = 50.dp)

                ChangeNicknameCard(
                    nickname = username,
                    onEditClick = {
                        navController.navigate(
                            SettingNavGraph.ChangeNickname.createRoute(
                                username
                            )
                        )
                    }
                )

                VerticalSpacer(height = 28.dp)

                ChangeMyPlanCard(
                    isPlanSet = myPlan != null,
                    myPlan = myPlan,
                    onEditClick = {
                        navController.navigate(SettingNavGraph.EditTrainingPlan.route)
                    })

                VerticalSpacer(height = 28.dp)

                TargetLanguageCard(onEditClick = {})

                VerticalSpacer(height = 28.dp)

                StudyNotificationCard(
                    checked = isSwitchChecked,
                    onCheckedChange = {
                        isSwitchChecked = it
                        // TODO if 문으로 뷰모델의 changePushAlarm 호출
                    }
                )

                VerticalSpacer(height = 10.dp)

                SmeemAlarmCard(
                    modifier = Modifier.padding(horizontal = 19.dp),
                    isDaySelected = { selectedTrainingTime.day.contains(Day.from(it)) },
                    onAlarmCardClick = {
                        if (isSwitchChecked) {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "trainingTime",
                                selectedTrainingTime
                            )

                            navController.navigate(
                                SettingNavGraph.EditTrainingTime.route
                            )
                        }
                    }
                )
            }
        }

        is UiState.Failure -> {
            // TODO : 실패 시 처리
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SettingScreenPreview() {
    SettingScreen(
        navController = rememberNavController(),
        viewModel = hiltViewModel(),
        modifier = Modifier
    )
}
