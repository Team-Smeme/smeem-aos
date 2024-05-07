package com.sopt.smeem.presentation.mypage.setting

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.components.SmeemButton
import com.sopt.smeem.presentation.compose.components.SmeemTextField
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray400
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.mypage.NICKNAME_MAX_LENGTH
import com.sopt.smeem.presentation.mypage.NICKNAME_MIN_LENGTH
import com.sopt.smeem.presentation.mypage.navigation.SettingNavGraph
import com.sopt.smeem.util.VerticalSpacer
import com.sopt.smeem.util.addFocusCleaner
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ChangeNicknameScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    nickname: String,
    viewModel: ChangeNickNameViewModel = hiltViewModel(),
) {
    val state = viewModel.collectAsState()
    val context = LocalContext.current

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
                viewModel.checkNicknameDuplicated(newValue.text)
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
            if (state.value.isDuplicated) {
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
            onClick = { viewModel.changeNickname(textFieldState.text) },
            modifier = Modifier.padding(horizontal = 18.dp),
            isButtonEnabled = textFieldState.text.length
                    in NICKNAME_MIN_LENGTH..NICKNAME_MAX_LENGTH
                    && textFieldState.text != nickname
                    && !state.value.isDuplicated
        )

        VerticalSpacer(height = 10.dp)
    }

    viewModel.collectSideEffect {
        when (it) {
            is ChangeNicknameSideEffect.ShowErrorToast -> {
                Toast.makeText(
                    context,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ChangeNicknameSideEffect.NavigateToSettingScreen -> {
                navController.navigate(SettingNavGraph.SettingMain.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = false
                    }
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ChangeNicknameScreenPreview() {
    ChangeNicknameScreen(navController = rememberNavController(), nickname = "이태하이")
}