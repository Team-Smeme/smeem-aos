package com.sopt.smeem.presentation.coach

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.smeem.R
import com.sopt.smeem.presentation.EventVM
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
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun CoachSurveyRoute(
    viewModel: CoachViewModel = hiltViewModel(),
    eventVm: EventVM = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    val state by viewModel.collectAsState()

    BackHandler {
        onCloseClick()
    }

    CoachSurveyScreen(
        state = state,
        onThumbSelected = { thumbSelection: ThumbSelection ->
            viewModel.onThumbSelected(thumbSelection)
        },
        onCloseClick = onCloseClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachSurveyScreen(
    state: CoachState,
    modifier: Modifier = Modifier,
    onThumbSelected: (ThumbSelection) -> Unit,
    onCloseClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
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
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
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
                style = Typography.headlineLarge.copy(fontSize = 28.sp)
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCoachSurveyScreen() {
    CoachSurveyScreen(
        state = CoachState(username = "haeti", totalCount = 10),
        onThumbSelected = {},
        onCloseClick = {}
    )
}
