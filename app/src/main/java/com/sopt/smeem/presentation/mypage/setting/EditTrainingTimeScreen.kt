package com.sopt.smeem.presentation.mypage.setting

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.R
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.presentation.compose.components.SmeemAlarmCard
import com.sopt.smeem.presentation.compose.components.SmeemButton
import com.sopt.smeem.presentation.compose.components.SmeemTimePickerDialog
import com.sopt.smeem.presentation.mypage.navigation.MyPageScreen
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun EditTrainingTimeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: EditTrainingTimeViewModel = hiltViewModel()
) {
    val trainingTimeResult = remember {
        navController.previousBackStackEntry?.savedStateHandle?.get<TrainingTime>("trainingTime")
    }
    LaunchedEffect(trainingTimeResult) {
        trainingTimeResult?.let { viewModel.initialize(it) }
    }

    val context = LocalContext.current
    var showTimePickDialog by rememberSaveable { mutableStateOf(false) }
    val selectedDays by viewModel.days.collectAsStateWithLifecycle()
    val selectedHour by viewModel.hour.collectAsStateWithLifecycle()
    val selectedMinute by viewModel.minute.collectAsStateWithLifecycle()
    val updatedTrainingTime = TrainingTime(selectedDays, selectedHour, selectedMinute)

    Column(modifier = modifier.fillMaxWidth()) {

        VerticalSpacer(height = 24.dp)

        SmeemAlarmCard(
            modifier = Modifier.padding(horizontal = 19.dp),
            isActive = true,
            selectedDays = selectedDays,
            trainingTime = updatedTrainingTime.asText(),
            onTimeCardClick = { showTimePickDialog = true },
            isContentClickable = true,
            onDayClick = { day ->
                viewModel.toggleDaySelection(day)
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        SmeemButton(
            text = stringResource(R.string.training_time_change_button),
            onClick = {
                viewModel.sendServer { t ->
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
                navController.navigate(MyPageScreen.Setting.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = false
                    }
                }
            },
            modifier = Modifier.padding(horizontal = 18.dp),
            isButtonEnabled = viewModel.canConfirmEdit()
        )

        VerticalSpacer(height = 10.dp)

    }

    if (showTimePickDialog) {
        SmeemTimePickerDialog(
            setShowDialog = { showTimePickDialog = it },
            onDismissRequest = { showTimePickDialog = false },
            onSaveButtonClick = { hour, minute ->
                viewModel.updateHourMinute(hour, minute)
                showTimePickDialog = false
            },
            initialHour = selectedHour,
            initialMinute = selectedMinute
        )
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewEditTrainingTimeScreen() {
    EditTrainingTimeScreen(
        navController = rememberNavController(),
        viewModel = hiltViewModel()
    )
}