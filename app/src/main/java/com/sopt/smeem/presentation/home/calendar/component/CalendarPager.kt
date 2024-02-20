package com.sopt.smeem.presentation.home.calendar.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.sopt.smeem.domain.model.Date
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CalendarPager(
    dateList: Array<List<Date>>,
    loadNextDates: (date: LocalDate) -> Unit,
    loadPrevDates: (date: LocalDate) -> Unit,
    beyondBoundsPageCount: Int = 1,
    content: @Composable (currentPage: Int) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        initialPageOffsetFraction = 0f
    ) { /*page count*/ 3 }
    LaunchedEffect(pagerState.settledPage) {
        if (pagerState.settledPage == 2) {
            loadNextDates(dateList[1][0].day)
            pagerState.scrollToPage(1)
        }
        if (pagerState.settledPage == 0) {
            loadPrevDates(dateList[0][0].day)
            pagerState.scrollToPage(1)
        }
    }
    HorizontalPager(
        state = pagerState,
        beyondBoundsPageCount = beyondBoundsPageCount,
        verticalAlignment = Alignment.Top,
    ) { currentPage ->
        content(currentPage)
    }
}