package com.sopt.smeem.presentation.home.calendar.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.sopt.smeem.domain.model.Date
import com.sopt.smeem.presentation.home.calendar.core.Period
import com.sopt.smeem.util.DateUtil
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CalendarPager(
    weekOrMonth: Period,
    dateList: Array<List<Date>>,
    loadNextDates: (date: LocalDate) -> Unit,
    loadPrevDates: (date: LocalDate) -> Unit,
    content: @Composable (currentPage: Int) -> Unit,
) {
    val isInitialized = rememberSaveable { mutableStateOf(false) }

    val initialPage = DateUtil.getCurrentDateIndex(weekOrMonth) - 1
    var currentPage by rememberSaveable { mutableStateOf(initialPage) }
    val pagerState = rememberPagerState(
        initialPage = initialPage
    ) { /*page count*/ DateUtil.getBetweenCount(weekOrMonth) }

    LaunchedEffect(pagerState.currentPage) {
        if (isInitialized.value) {
            if (currentPage > pagerState.currentPage) {
                loadPrevDates(dateList[0][0].day)
            } else if (currentPage < pagerState.currentPage) {
                loadNextDates(dateList[1][0].day)
            }
            currentPage = pagerState.currentPage
        }
    }
    LaunchedEffect(Unit) {
        isInitialized.value = true
    }
    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top,
    ) { page ->
        if (page in pagerState.currentPage - 1..pagerState.currentPage + 1) {
            val calendarPage = page - pagerState.currentPage + 1
            content(calendarPage)
        }
    }
}