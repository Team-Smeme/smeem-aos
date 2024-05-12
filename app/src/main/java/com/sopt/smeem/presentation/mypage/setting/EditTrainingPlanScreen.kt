package com.sopt.smeem.presentation.mypage.setting

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.components.LoadingScreen
import com.sopt.smeem.presentation.compose.components.SmeemButton
import com.sopt.smeem.presentation.mypage.components.TrainingPlanCard
import com.sopt.smeem.util.UiState
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun EditTrainingPlanScreen(
    modifier: Modifier = Modifier,
    onEditSuccess: () -> Unit,
    editTrainingPlanViewModel: EditTrainingPlanViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var selectedItemId by rememberSaveable { mutableIntStateOf(0) }
    val trainingPlanState by editTrainingPlanViewModel.trainingPlans.collectAsStateWithLifecycle()

    when (val uiState = trainingPlanState) {
        is UiState.Idle -> {
            Box(modifier = modifier.fillMaxSize())
        }

        is UiState.Loading -> {
            LoadingScreen(modifier = modifier)
        }

        is UiState.Success -> {
            val trainingPlanList = uiState.data

            Column(modifier = modifier.fillMaxSize()) {
                VerticalSpacer(height = 24.dp)

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(trainingPlanList.size) { index ->
                        val item = trainingPlanList[index]

                        TrainingPlanCard(
                            isSelected = selectedItemId == item.id,
                            planContent = item.content,
                            onClick = { selectedItemId = item.id },
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                SmeemButton(
                    text = stringResource(id = R.string.edit_training_plan_button),
                    onClick = {
                        editTrainingPlanViewModel.editTrainingPlan(
                            planId = selectedItemId,
                            onSuccess = onEditSuccess,
                            onError = { t ->
                                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier.padding(horizontal = 18.dp),
                    isButtonEnabled = selectedItemId != 0
                )

                VerticalSpacer(height = 10.dp)

            }
        }

        is UiState.Failure -> {
            Toast.makeText(context, uiState.error.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditTrainingPlanScreenPreview() {
    EditTrainingPlanScreen(onEditSuccess = {})
}