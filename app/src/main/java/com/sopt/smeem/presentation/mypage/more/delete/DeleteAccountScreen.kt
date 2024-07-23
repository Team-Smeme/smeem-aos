package com.sopt.smeem.presentation.mypage.more.delete

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.R
import com.sopt.smeem.domain.dto.WithdrawDto
import com.sopt.smeem.domain.model.WithdrawType
import com.sopt.smeem.presentation.compose.components.SmeemButton
import com.sopt.smeem.presentation.compose.components.SmeemDialog
import com.sopt.smeem.presentation.compose.components.SmeemTextField
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.mypage.REASON_MAX_LENGTH
import com.sopt.smeem.presentation.mypage.components.SelectCard
import com.sopt.smeem.presentation.mypage.more.MoreViewModel
import com.sopt.smeem.presentation.splash.SplashLoginActivity
import com.sopt.smeem.util.TextUtil.whitespaceToEmpty
import com.sopt.smeem.util.VerticalSpacer
import com.sopt.smeem.util.addFocusCleaner
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun DeleteAccountScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val moreViewModel: MoreViewModel = hiltViewModel()

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var textFieldState by remember { mutableStateOf(TextFieldValue(text = "")) }
    val focusRequester = remember { FocusRequester() }

    var selectedType by rememberSaveable { mutableStateOf(WithdrawType.INSTABILITY) }
    val (showDeleteDialog, setShowDeleteDialog) = rememberSaveable { mutableStateOf(false) }

    if (showDeleteDialog) {
        SmeemDialog(
            setShowDialog = setShowDeleteDialog,
            title = stringResource(R.string.smeem_dialog_delete_account_title),
            content = stringResource(R.string.smeem_dialog_delete_dialog_content),
            onConfirmButtonClick = {
                moreViewModel.withdrawal(
                    type = selectedType,
                    reason = textFieldState.text,
                    onSuccess = {
                        context.startActivity(Intent(context, SplashLoginActivity::class.java))
                        (context as? Activity)?.finishAffinity()
                    },
                    onError = { t ->
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }
                )
            })
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .addFocusCleaner(focusManager)
    ) {
        VerticalSpacer(height = 14.dp)

        Text(
            text = stringResource(R.string.delete_account_title),
            style = Typography.headlineMedium,
            color = black,
            modifier = Modifier.padding(start = 26.dp)
        )

        VerticalSpacer(height = 6.dp)

        Text(
            text = stringResource(R.string.delete_account_subtitle),
            style = Typography.bodySmall.copy(
                lineHeight = 22.sp
            ),
            color = black,
            modifier = Modifier.padding(start = 26.dp)
        )

        VerticalSpacer(height = 12.dp)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 1000.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(WithdrawType.entries.size) { index ->
                val type = WithdrawType.entries[index]

                SelectCard(
                    isSelected = selectedType == type,
                    selectContent = type.text,
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        selectedType = type
                    },
                )
            }
        }

        VerticalSpacer(height = 27.dp)

        Text(
            text = stringResource(R.string.delete_account_reason_label),
            style = Typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                lineHeight = 22.sp
            ),
            color = black,
            modifier = Modifier.padding(start = 26.dp)
        )

        VerticalSpacer(height = 22.dp)

        SmeemTextField(
            value = textFieldState,
            onValueChange = { newValue ->
                if (newValue.text.length <= REASON_MAX_LENGTH) {
                    textFieldState = newValue
                }
            },
            placeholder = stringResource(R.string.delete_account_reason_placeholder),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }),
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .focusRequester(focusRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    }
                }
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        textFieldState = textFieldState.copy(
                            selection = TextRange(
                                textFieldState.text.length
                            )
                        )
                    }
                },
            minLines = 2,
            backgroundColor = gray100,
            cursorColor = black,
            hasBorder = false,
            textStyle = Typography.bodySmall.copy(
                color = black,
                lineHeight = 22.sp
            )
        )

        Spacer(Modifier.weight(1f))

        VerticalSpacer(height = 12.dp)

        SmeemButton(
            text = stringResource(R.string.delete_account_navi_title),
            onClick = {
                focusManager.clearFocus()
                keyboardController?.hide()
                setShowDeleteDialog(true)
            },
            modifier = Modifier.padding(horizontal = 18.dp),
            isButtonEnabled = WithdrawDto(
                selectedType,
                textFieldState.text.whitespaceToEmpty()
            ).isValidContent()
        )

        VerticalSpacer(height = 16.dp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeleteAccountScreenPreview() {
    DeleteAccountScreen(navController = rememberNavController(), modifier = Modifier)
}