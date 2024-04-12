package com.sopt.smeem.presentation.home.calendar.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import com.sopt.smeem.domain.model.Date
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CalendarPager(
    dateList: Array<List<Date>>,
    loadNextDates: (date: LocalDate) -> Unit,
    loadPrevDates: (date: LocalDate) -> Unit,
    content: @Composable (currentPage: Int) -> Unit,
) {
    val initialized = rememberSaveable { mutableStateOf(false) }
    val pagerState = rememberPagerState(initialPage = 1) { /*page count*/ 3 }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == 0 && initialized.value) {
            loadPrevDates(dateList[0][0].day)
            pagerState.scrollToPage(1)
        }

        if (pagerState.currentPage == 2) {
            loadNextDates(dateList[1][0].day)
            pagerState.scrollToPage(1)
        }
    }

    LaunchedEffect(Unit) {
        initialized.value = true
    }

    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top,
    ) { currentPage ->
        content(currentPage)
    }
}
