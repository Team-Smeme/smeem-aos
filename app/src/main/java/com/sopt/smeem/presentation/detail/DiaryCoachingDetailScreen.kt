package com.sopt.smeem.presentation.detail

import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.components.HighlightedDiary
import com.sopt.smeem.presentation.compose.components.SmeemPagerIndicator
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.gray500
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.util.HorizontalSpacer
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun DiaryCoachingDetailScreen(
    diaryDetail: DiaryDetail,
    modifier: Modifier = Modifier,
) {
    val correctedCount = diaryDetail.corrections.count { it.isCorrected }
    val correctedCorrections = diaryDetail.corrections.filter { it.isCorrected }

    val scrollStateTop = rememberScrollState()
    val scrollStateBottom = rememberScrollState()

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { correctedCount }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 18.dp, vertical = 16.dp)
                .verticalScroll(scrollStateTop)
        ) {
            val highlightedContent = HighlightedDiary(
                diaryDetail.corrections,
                correctedCorrections,
                pagerState.currentPage
            )

            BasicText(
                text = highlightedContent,
                style = Typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacer(24.dp)

            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = diaryDetail.createdAt,
                        style = Typography.labelMedium,
                        color = gray500
                    )
                }

                VerticalSpacer(4.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = diaryDetail.writerUsername,
                        style = Typography.labelMedium,
                        color = gray500
                    )
                }
            }
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
