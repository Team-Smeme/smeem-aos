package com.sopt.smeem.presentation.mypage.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.presentation.compose.components.SmeemAlarmCard

@Composable
fun EditTrainingTimeScreen(
    modifier: Modifier = Modifier,
    trainingTime: TrainingTime
) {

    Column(modifier = modifier.fillMaxWidth()) {
        SmeemAlarmCard(
            modifier = Modifier.padding(horizontal = 19.dp),
            isDaySelected = { trainingTime.days.contains(Day.from(it)) },
            trainingTime = "${trainingTime.asHour()}:${trainingTime.asMinute()} ${trainingTime.asAmpm()}",
        )
    }


}

@Preview
@Composable
fun PreviewEditTrainingTimeScreen() {
    EditTrainingTimeScreen(
        trainingTime = TrainingTime(setOf(Day.MON, Day.THU), 10, 30)
    )
}