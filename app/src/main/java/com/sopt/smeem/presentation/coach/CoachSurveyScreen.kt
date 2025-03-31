package com.sopt.smeem.presentation.coach

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.smeem.R
import com.sopt.smeem.domain.model.SurveyType
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.presentation.coach.CoachSurveyConstants.REASON_MAX_LENGTH
import com.sopt.smeem.presentation.compose.components.SmeemButton
import com.sopt.smeem.presentation.compose.components.SmeemChip
import com.sopt.smeem.presentation.compose.components.SmeemTextField
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.gray200
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.pointInactive
import com.sopt.smeem.presentation.compose.theme.pointInactive30
import com.sopt.smeem.presentation.compose.theme.white
import com.sopt.smeem.util.HorizontalSpacer
import com.sopt.smeem.util.VerticalSpacer
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CoachSurveyRoute(
    viewModel: CoachViewModel = hiltViewModel(),
    eventVm: EventVM = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CoachSideEffect.NavigateToHome -> {
                onCloseClick()
            }

            is CoachSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    BackHandler {
        onCloseClick()
    }

    CoachSurveyScreen(
        state = state,
        onThumbSelected = { thumbSelection: ThumbSelection ->
            viewModel.onThumbSelected(thumbSelection)
        },
        onSurveyTypeSelected = { surveyType: SurveyType ->
            viewModel.onSurveyTypeSelected(surveyType)
        },
        onReasonChange = { reason: String ->
            viewModel.updateSurveyReason(reason)
        },
        onCloseClick = onCloseClick,
        onSubmitClick = {
            viewModel.postSurvey()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachSurveyScreen(
    state: CoachState,
    modifier: Modifier = Modifier,
    onThumbSelected: (ThumbSelection) -> Unit,
    onSurveyTypeSelected: (SurveyType) -> Unit,
    onReasonChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onSubmitClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    var textFieldState by remember { mutableStateOf(TextFieldValue(text = "")) }


    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            },
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
                    containerColor = white
                ),
                title = {},
                navigationIcon = {},
                actions = {
                    Text(
                        text = stringResource(R.string.coach_topappbar_action_text),
                        style = Typography.bodyMedium,
                        color = black,
                        modifier = Modifier
                            .padding(end = 18.dp)
                            .clickable { onCloseClick() }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${state.username}님,\n오늘까지 코칭을 ${state.totalCount}번 받으셨네요!\n꾸준함이 대단해요! \uD83D\uDC4F",
                color = black,
                style = Typography.bodyMedium,
                textAlign = TextAlign.Center,
            )

            VerticalSpacer(16.dp)

            Text(
                text = "AI 코칭, 어떠셨나요?",
                color = black,
                style = Typography.headlineSmall,
            )

            VerticalSpacer(16.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ThumbsCard(
                    thumbType = ThumbSelection.THUMB_UP,
                    isSelected = state.thumbSelection == ThumbSelection.THUMB_UP,
                    isUnselected = state.thumbSelection == ThumbSelection.THUMB_DOWN,
                    onClick = { onThumbSelected(ThumbSelection.THUMB_UP) }
                )

                HorizontalSpacer(12.dp)

                ThumbsCard(
                    thumbType = ThumbSelection.THUMB_DOWN,
                    isSelected = state.thumbSelection == ThumbSelection.THUMB_DOWN,
                    isUnselected = state.thumbSelection == ThumbSelection.THUMB_UP,
                    onClick = { onThumbSelected(ThumbSelection.THUMB_DOWN) }
                )
            }

            VerticalSpacer(16.dp)

            if (state.thumbSelection == ThumbSelection.THUMB_DOWN) {
                SurveyTypeChips(
                    selectedTypes = state.selectedSurveyTypes,
                    onSurveyTypeSelected = onSurveyTypeSelected
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (state.thumbSelection != ThumbSelection.NONE) {
                        SmeemTextField(
                            value = textFieldState,
                            onValueChange = { newValue ->
                                if (newValue.text.length <= REASON_MAX_LENGTH) {
                                    textFieldState = newValue
                                    onReasonChange(newValue.text)
                                }
                            },
                            placeholder = "(선택) 이유를 적어주세요.",
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 18.dp)
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        coroutineScope.launch {
                                            delay(200)
                                            scrollState.animateScrollTo(scrollState.maxValue)
                                        }
                                        textFieldState = textFieldState.copy(
                                            selection = TextRange(
                                                textFieldState.text.length
                                            )
                                        )
                                    }
                                },
                            backgroundColor = gray100,
                            cursorColor = black,
                            minLines = 2,
                            hasBorder = false,
                            textStyle = Typography.bodySmall.copy(
                                color = black,
                            )
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    VerticalSpacer(15.dp)

                    SmeemButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        text = "의견 보내기",
                        onClick = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            onSubmitClick()
                        },
                        isButtonEnabled = state.thumbSelection != ThumbSelection.NONE,
                    )

                    VerticalSpacer(20.dp)
                }
            }
        }
    }
}

@Composable
fun ThumbsCard(
    thumbType: ThumbSelection,
    isSelected: Boolean,
    isUnselected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = when {
        isSelected -> pointInactive
        isUnselected -> gray100
        else -> pointInactive30
    }

    val iconColor = when {
        isSelected -> point
        isUnselected -> gray200
        else -> pointInactive
    }

    val iconRes = remember(thumbType) {
        if (thumbType == ThumbSelection.THUMB_UP) R.drawable.ic_thumb_up
        else R.drawable.ic_thumb_down
    }

    val contentDescription = if (thumbType == ThumbSelection.THUMB_UP) {
        stringResource(R.string.thumbs_up_description)
    } else {
        stringResource(R.string.thumbs_down_description)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(color = backgroundColor, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .clickable { onClick() }
                .padding(21.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                colorFilter = ColorFilter.tint(color = iconColor)
            )
        }

        VerticalSpacer(12.dp)

        Text(
            text = contentDescription,
            color = black,
            style = Typography.bodyMedium
        )
    }
}

@Composable
fun SurveyTypeChips(
    selectedTypes: Set<SurveyType>,
    onSurveyTypeSelected: (SurveyType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            val firstRowTypes = persistentListOf(
                SurveyType.HARD_TO_UNDERSTAND,
                SurveyType.TOO_SHORT,
                SurveyType.FEEDBACK_ERROR
            )

            firstRowTypes.forEach { surveyType ->
                SmeemChip(
                    text = surveyType.text,
                    isSelected = selectedTypes.contains(surveyType),
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = { onSurveyTypeSelected(surveyType) }
                )
            }
        }

        VerticalSpacer(10.dp)

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            val secondRowTypes = persistentListOf(
                SurveyType.WORD_FEEDBACK_LACK,
                SurveyType.GRAMMAR_FEEDBACK_LACK
            )

            secondRowTypes.forEach { surveyType ->
                SmeemChip(
                    text = surveyType.text,
                    isSelected = selectedTypes.contains(surveyType),
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = { onSurveyTypeSelected(surveyType) }
                )
            }
        }
    }
}

class ThumbsCardPreviewProvider : PreviewParameterProvider<ThumbsCardPreviewState> {
    override val values = sequenceOf(
        // Initial state (THUMB_UP)
        ThumbsCardPreviewState(ThumbSelection.THUMB_UP, false, false),
        // Initial state (THUMB_DOWN)
        ThumbsCardPreviewState(ThumbSelection.THUMB_DOWN, false, false),
        // Selected state (THUMB_UP)
        ThumbsCardPreviewState(ThumbSelection.THUMB_UP, true, false),
        // Selected state (THUMB_DOWN)
        ThumbsCardPreviewState(ThumbSelection.THUMB_DOWN, true, false),
        // Unselected state (THUMB_UP)
        ThumbsCardPreviewState(ThumbSelection.THUMB_UP, false, true),
        // Unselected state (THUMB_DOWN)
        ThumbsCardPreviewState(ThumbSelection.THUMB_DOWN, false, true)
    )
}

data class ThumbsCardPreviewState(
    val thumbType: ThumbSelection,
    val isSelected: Boolean,
    val isUnselected: Boolean
)

@Preview(showBackground = true)
@Composable
fun ThumbsCardPreview(@PreviewParameter(ThumbsCardPreviewProvider::class) state: ThumbsCardPreviewState) {
    ThumbsCard(
        thumbType = state.thumbType,
        isSelected = state.isSelected,
        isUnselected = state.isUnselected,
        onClick = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CoachSurveyScreenPreview() {
    CoachSurveyScreen(
        state = CoachState(username = "haeti", totalCount = 10),
        onThumbSelected = {},
        onSurveyTypeSelected = {},
        onCloseClick = {},
        onReasonChange = {},
        onSubmitClick = {}
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CoachSurveyScreenThumbsUpPreview() {
    CoachSurveyScreen(
        state = CoachState(
            username = "haeti",
            totalCount = 10,
            thumbSelection = ThumbSelection.THUMB_UP
        ),
        onThumbSelected = {},
        onSurveyTypeSelected = {},
        onCloseClick = {},
        onReasonChange = {},
        onSubmitClick = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CoachSurveyScreenThumbsDownPreview() {
    CoachSurveyScreen(
        state = CoachState(
            username = "haeti",
            totalCount = 10,
            thumbSelection = ThumbSelection.THUMB_DOWN
        ),
        onThumbSelected = {},
        onSurveyTypeSelected = {},
        onCloseClick = {},
        onReasonChange = {},
        onSubmitClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SurveyTypeChipsPreview() {
    SurveyTypeChips(
        selectedTypes = setOf(),
        onSurveyTypeSelected = {},
    )
}

private object CoachSurveyConstants {
    const val REASON_MAX_LENGTH = 300
}
