package com.sopt.smeem.presentation.home.calendar.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.sopt.smeem.domain.model.Date
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MonthlyCalendar(
    dateList: Array<List<Date>>,
    selectedDate: LocalDate,
    currentMonth: YearMonth,
    loadDatesForMonth: (YearMonth) -> Unit,
    onDayClick: (LocalDate) -> Unit
) {
    val itemWidth = (LocalConfiguration.current.screenWidthDp.dp - 38.dp) / 7
    CalendarPager(
        dateList = dateList,
        loadNextDates = { loadDatesForMonth(currentMonth) },
        loadPrevDates = { loadDatesForMonth(currentMonth.minusMonths(2)) }
    ) { currentPage ->
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            dateList[currentPage].forEachIndexed { index, date ->
                Box(
                    modifier = Modifier
                        .width(itemWidth),
                    contentAlignment = Alignment.Center
                ) {
                    DayItem(
                        date = date.day,
                        isSelected = date.isCurrentMonth && selectedDate == date.day,
                        isCurrentMonth = date.isCurrentMonth,
                        isDiaryWritten = date.isDiaryWritten,
                        onDayClick = { onDayClick(date.day) },
                        isFirstWeek = index < 7,
                        isSixWeeks = dateList[currentPage].size > 35
                    )
                }
            }
        }
    }
}