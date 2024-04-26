package com.sopt.smeem.presentation.mypage.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.presentation.compose.components.SmeemAlarmCard
import com.sopt.smeem.presentation.compose.components.SmeemTimePickerDialog
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun EditTrainingTimeScreen(
    modifier: Modifier = Modifier,
    trainingTime: TrainingTime,
) {

    var showTimePickDialog by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {

        VerticalSpacer(height = 24.dp)

        SmeemAlarmCard(
            modifier = Modifier.padding(horizontal = 19.dp),
            isDaySelected = { trainingTime.days.contains(Day.from(it)) },
            trainingTime = "${trainingTime.asHour()}:${trainingTime.asMinute()} ${trainingTime.asAmpm()}",
            onTimeCardClick = { showTimePickDialog = true },
            isContentClickable = true
        )

        if (showTimePickDialog) {
            SmeemTimePickerDialog(
                setShowDialog = { showTimePickDialog = it },
                onDismissRequest = { showTimePickDialog = false },
                onSaveButtonClick = { showTimePickDialog = false }
            )
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewEditTrainingTimeScreen() {
    EditTrainingTimeScreen(
        trainingTime = TrainingTime(setOf(Day.MON, Day.THU), 10, 30),
    )
}