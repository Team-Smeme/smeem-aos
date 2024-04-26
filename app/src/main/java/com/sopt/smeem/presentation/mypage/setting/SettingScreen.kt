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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.presentation.compose.components.SmeemAlarmCard
import com.sopt.smeem.presentation.mypage.components.ChangeMyPlanCard
import com.sopt.smeem.presentation.mypage.components.ChangeNicknameCard
import com.sopt.smeem.presentation.mypage.components.StudyNotificationCard
import com.sopt.smeem.presentation.mypage.components.TargetLanguageCard
import com.sopt.smeem.presentation.mypage.navigation.SettingNavGraph
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun SettingScreen(
    navController: NavController,
    modifier: Modifier
) {

    // TODO 초기값 서버에서 가져오기
    val mockNickname = "이태하이"
    val mockMyPlan = "주 3회 일기 작성하기"
    var isSwitchChecked by rememberSaveable { mutableStateOf(true) }
    val mockDays = setOf(Day.MON, Day.TUE, Day.THU)
    val mockTrainingTime = TrainingTime(mockDays, 12, 30)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VerticalSpacer(height = 50.dp)

        ChangeNicknameCard(
            nickname = mockNickname,
            onEditClick = {
                navController.navigate(SettingNavGraph.ChangeNickname.createRoute(mockNickname))
            }
        )

        VerticalSpacer(height = 28.dp)

        ChangeMyPlanCard(myPlan = mockMyPlan, onEditClick = {})

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
            isDaySelected = { mockTrainingTime.days.contains(Day.from(it)) },
            onAlarmCardClick = {
                if (isSwitchChecked) {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "trainingTime",
                        mockTrainingTime
                    )

                    navController.navigate(
                        SettingNavGraph.EditTrainingTime.route
                    )
                }
            }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SettingScreenPreview() {
    SettingScreen(navController = rememberNavController(), modifier = Modifier)
}
