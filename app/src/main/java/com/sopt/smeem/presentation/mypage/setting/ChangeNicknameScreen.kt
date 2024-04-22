package com.sopt.smeem.presentation.mypage.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.components.SmeemButton
import com.sopt.smeem.presentation.compose.components.SmeemTextField
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray400
import com.sopt.smeem.util.HorizontalSpacer
import com.sopt.smeem.util.VerticalSpacer
import com.sopt.smeem.util.addFocusCleaner

@Composable
fun ChangeNicknameScreen(
    modifier: Modifier = Modifier,
    nickname: String
) {
    var newNickName by rememberSaveable { mutableStateOf(nickname) }

    Column(
        modifier = modifier.addFocusCleaner(LocalFocusManager.current),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        VerticalSpacer(height = 14.dp)

        SmeemTextField(
            text = newNickName,
            onValueChange = { newNickName = it },
            modifier = Modifier.padding(horizontal = 18.dp)
        )

        VerticalSpacer(height = 10.dp)

        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.entrance_nickname_caption),
                style = Typography.labelSmall,
                color = gray400,
            )

            HorizontalSpacer(width = 18.dp)
        }

        Spacer(modifier = Modifier.weight(1f))


        SmeemButton(
            text = stringResource(id = R.string.my_page_change_nickname_button),
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(horizontal = 18.dp)
        )

        VerticalSpacer(height = 10.dp)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ChangeNicknameScreenPreview() {
    ChangeNicknameScreen(nickname = "이태하이")
}