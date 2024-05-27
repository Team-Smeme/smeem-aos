package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.theme.gray200
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.white

@Composable
fun StudyNotificationCard(
    modifier: Modifier = Modifier,
    checked: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {

    SmeemContents(title = stringResource(id = R.string.my_page_study_alarm)) {
        SmeemCard(text = stringResource(id = R.string.my_page_training_push_alarm)) {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = white,
                    checkedTrackColor = point,
                    uncheckedThumbColor = white,
                    uncheckedTrackColor = gray200,
                    uncheckedBorderColor = gray200,
                ),
                modifier = modifier
                    .scale(0.69f)
                    .padding(end = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudyNotificationCardPreview() {
    StudyNotificationCard(onCheckedChange = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudyNotificationCardPreviewUnchecked() {
    StudyNotificationCard(checked = false, onCheckedChange = {})
}




