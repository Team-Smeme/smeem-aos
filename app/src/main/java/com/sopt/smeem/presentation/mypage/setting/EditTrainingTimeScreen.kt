package com.sopt.smeem.presentation.mypage.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.R
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.presentation.compose.components.SmeemAlarmCard
import com.sopt.smeem.presentation.compose.components.SmeemButton
import com.sopt.smeem.presentation.compose.components.SmeemTimePickerDialog
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun EditTrainingTimeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    trainingTime: TrainingTime,
    viewModel: EditTrainingTimeViewModel = hiltViewModel()
) {

    viewModel.originalTime = trainingTime

    var showTimePickDialog by rememberSaveable { mutableStateOf(false) }
    val selectedDays by viewModel.days.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxWidth()) {

        VerticalSpacer(height = 24.dp)

        SmeemAlarmCard(
            modifier = Modifier.padding(horizontal = 19.dp),
            isActive = true,
            selectedDays = selectedDays,
            trainingTime = "${trainingTime.asHour()}:${trainingTime.asMinute()} ${trainingTime.asAmpm()}",
            onTimeCardClick = { showTimePickDialog = true },
            isContentClickable = true,
            onDayClick = { day ->
                viewModel.toggleDaySelection(day)
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        SmeemButton(
            text = stringResource(R.string.training_time_change_button),
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(horizontal = 18.dp),
            isButtonEnabled = true
        )

        VerticalSpacer(height = 10.dp)

    }

    if (showTimePickDialog) {
        SmeemTimePickerDialog(
            setShowDialog = { showTimePickDialog = it },
            onDismissRequest = { showTimePickDialog = false },
            onSaveButtonClick = { }
        )
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewEditTrainingTimeScreen() {
    EditTrainingTimeScreen(
        navController = rememberNavController(),
        trainingTime = TrainingTime(setOf(Day.MON, Day.THU), 10, 30),
    )
}