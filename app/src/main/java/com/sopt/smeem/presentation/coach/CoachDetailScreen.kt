package com.sopt.smeem.presentation.coach

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sopt.smeem.R
import com.sopt.smeem.domain.dto.CorrectionDto
import com.sopt.smeem.event.AmplitudeEventType
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.presentation.compose.components.HighlightedDiary
import com.sopt.smeem.presentation.compose.components.SmeemPagerIndicator
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.white
import com.sopt.smeem.util.HorizontalSpacer
import com.sopt.smeem.util.VerticalSpacer
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun CoachDetailRoute(
    navController: NavController,
    viewModel: CoachViewModel = hiltViewModel(),
    eventVm: EventVM = hiltViewModel(),
    onCloseClick: () -> Unit,
) {
    val state by viewModel.collectAsState()

    BackHandler {
        onCloseClick()
    }

    if (state.isLoading) {
        eventVm.sendEvent(AmplitudeEventType.COACHING_LOAD_VIEW)
        CoachLoadingScreen()
    } else {
        eventVm.sendEvent(AmplitudeEventType.COACHING_RESULT_VIEW)
        CoachDetailScreen(
            state = state,
            onCloseClick = onCloseClick,
        )
    }
}

@Composable
fun CoachLoadingScreen(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.smeem_loading))

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxWidth()
                .height(164.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )

        VerticalSpacer(8.dp)

        Text(
            text = stringResource(R.string.lottie_animation_description),
            style = Typography.bodySmall,
            color = black,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachDetailScreen(
    state: CoachState,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
) {
    val correctedCount = state.corrections.count { it.isCorrected }
    val correctedCorrections = state.corrections.filter { it.isCorrected }

    val scrollStateTop = rememberScrollState()
    val scrollStateBottom = rememberScrollState()

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { correctedCount }
    )

    val coachBannerText = when {
        correctedCount == 0 -> stringResource(R.string.coach_banner_text_0)
        correctedCount == 1 -> stringResource(R.string.coach_banner_text_1)
        correctedCount >= 2 -> stringResource(R.string.coach_banner_text_2)
        else -> stringResource(R.string.coach_banner_text_default)
    }

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
                .padding(it)
        ) {
            Text(
                text = coachBannerText,
                style = Typography.bodySmall.copy(lineHeight = 22.sp),
                color = black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 18.dp, vertical = 16.dp)
                    .verticalScroll(scrollStateTop)
            ) {
                val highlightedContent = HighlightedDiary(
                    state.corrections,
                    correctedCorrections,
                    pagerState.currentPage
                )

                BasicText(
                    text = highlightedContent,
                    style = Typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (correctedCount > 0) {
                HorizontalDivider(
                    color = gray100,
                    thickness = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 20.dp, start = 18.dp, end = 18.dp, bottom = 10.dp)
                ) { page ->
                    val correction = correctedCorrections[page]

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollStateBottom)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .fillMaxHeight()
                                    .background(color = black)
                            )

                            HorizontalSpacer(8.dp)

                            Text(
                                text = stringResource(R.string.coach_detail_my_diary),
                                style = Typography.bodyMedium,
                                color = black
                            )
                        }

                        VerticalSpacer(8.dp)

                        Text(
                            text = correction.originalSentence,
                            style = Typography.labelMedium,
                        )

                        VerticalSpacer(20.dp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .fillMaxHeight()
                                    .background(color = point)
                            )

                            HorizontalSpacer(8.dp)

                            Text(
                                text = stringResource(R.string.coach_detail_corrected_sentence),
                                style = Typography.bodyMedium,
                                color = point
                            )
                        }

                        VerticalSpacer(8.dp)

                        Text(
                            text = correction.correctedSentence,
                            style = Typography.labelMedium,
                            color = point,
                        )

                        VerticalSpacer(8.dp)

                        Text(
                            text = correction.reason!!,
                            style = Typography.labelMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = gray100,
                                    shape = RoundedCornerShape(3.dp)
                                )
                                .padding(12.dp),
                            textAlign = TextAlign.Start,
                            color = black
                        )
                    }
                }

                SmeemPagerIndicator(
                    currentPage = pagerState.currentPage,
                    pageCount = correctedCount,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CoachLoadingScreenPreview() {
    CoachLoadingScreen()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CoachDetailScreenPreview() {
    CoachDetailScreen(state = CoachState(corrections = mockCorrection.corrections))
}

val mockCorrection = CoachState(
    corrections = persistentListOf(
        CorrectionDto(
            originalSentence = "오늘은 어떤 일이 있었나요?",
            correctedSentence = "오늘은 어떤 일이 있었나요?",
            isCorrected = false,
            reason = "문장이 너무 짧습니다.",
        ),
        CorrectionDto(
            originalSentence = "I have went to the park yesterday",
            correctedSentence = "I went to the park yesterday",
            isCorrected = true,
            reason = "현재완료 시제인 \"have went\"는 과거 시제인 \"went\"로 바꾸는 것이 맞습니다. \"yesterday\"와 함께 사용할 때는 단순 과거 시제를 사용해야 합니다.",
        ),
        CorrectionDto(
            originalSentence = "오늘은 어떤 일이 있었나요?00",
            correctedSentence = "오늘은 어떤 일이 있었나요?00",
            isCorrected = false,
            reason = "문장이 너무 짧습니다.",
        ),
        CorrectionDto(
            originalSentence = "오늘은 어떤 일이 있었나요?22",
            correctedSentence = "오늘은 어떤 일이 있었나요?22",
            isCorrected = true,
            reason = "문장이 너무 짧습니다.",
        ),
    )
)
