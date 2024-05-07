package com.sopt.smeem.presentation.mypage.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.components.SmeemButton
import com.sopt.smeem.presentation.compose.components.SmeemTextField
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray400
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.mypage.NICKNAME_MAX_LENGTH
import com.sopt.smeem.presentation.mypage.NICKNAME_MIN_LENGTH
import com.sopt.smeem.util.VerticalSpacer
import com.sopt.smeem.util.addFocusCleaner

@Composable
fun ChangeNicknameScreen(
    modifier: Modifier = Modifier,
    nickname: String,
    isDuplicated: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var textFieldState by remember { mutableStateOf(TextFieldValue(text = nickname)) }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        VerticalSpacer(height = 14.dp)

        SmeemTextField(
            value = textFieldState,
            onValueChange = { newValue ->
                textFieldState = newValue
            },
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        textFieldState = textFieldState.copy(
                            selection = TextRange(
                                textFieldState.text.length
                            )
                        )
                    }
                }
        )

        VerticalSpacer(height = 10.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {
            if (isDuplicated) {
                Text(
                    text = stringResource(id = R.string.entrance_nickname_duplicated),
                    style = Typography.labelSmall,
                    color = point,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.entrance_nickname_caption),
                style = Typography.labelSmall,
                color = gray400,
            )
        }

        Spacer(modifier = Modifier.weight(1f))


        SmeemButton(
            text = stringResource(id = R.string.my_page_change_nickname_button),
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(horizontal = 18.dp),
            isButtonEnabled = textFieldState.text.length
                    in NICKNAME_MIN_LENGTH..NICKNAME_MAX_LENGTH && textFieldState.text != nickname
        )

        VerticalSpacer(height = 10.dp)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ChangeNicknameScreenPreview() {
    ChangeNicknameScreen(nickname = "이태하이")
}