package com.sopt.smeem.presentation.mypage.setting

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.components.SmeemButton
import com.sopt.smeem.presentation.mypage.components.TrainingPlanCard
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun EditTrainingPlanScreen(
    modifier: Modifier = Modifier,
) {
    var selectedItemId by rememberSaveable { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxSize()) {
        VerticalSpacer(height = 24.dp)

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(4) { index ->
                val itemId = index + 1

                TrainingPlanCard(
                    isSelected = selectedItemId == itemId,
                    planId = itemId,
                    onClick = { selectedItemId = itemId },
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        SmeemButton(
            text = stringResource(id = R.string.edit_training_plan_button),
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(horizontal = 18.dp),
            isButtonEnabled = selectedItemId != 0
        )

        VerticalSpacer(height = 10.dp)

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditTrainingPlanScreenPreview() {
    EditTrainingPlanScreen()
}