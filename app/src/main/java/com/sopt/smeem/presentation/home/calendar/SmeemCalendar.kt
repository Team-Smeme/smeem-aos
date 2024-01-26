package com.sopt.smeem.presentation.home.calendar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sopt.smeem.domain.model.Date
import com.sopt.smeem.presentation.home.HomeViewModel
import com.sopt.smeem.presentation.home.calendar.component.CalendarToggleArea
import com.sopt.smeem.presentation.home.calendar.component.CalendarTitle
import com.sopt.smeem.presentation.home.calendar.component.MonthlyCalendar
import com.sopt.smeem.presentation.home.calendar.component.WeekLabel
import com.sopt.smeem.presentation.home.calendar.component.WeeklyCalendar
import com.sopt.smeem.presentation.home.calendar.core.CalendarIntent
import com.sopt.smeem.presentation.home.calendar.core.Period
import com.sopt.smeem.util.getWeekStartDate
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun SmeemCalendarImpl(
    onDayClick: (LocalDate) -> Unit
) {
    val viewModel: HomeViewModel = viewModel()
    val dateList = viewModel.visibleDates.collectAsState()
    val selectedDate = viewModel.selectedDate.collectAsState()
    val isCalendarExpanded = viewModel.isCalendarExpanded.collectAsState()
    val currentMonth = viewModel.currentMonth.collectAsState()

    SmeemCalendarImpl(
        dateList = dateList.value,
        selectedDate = selectedDate.value,
        currentMonth = currentMonth.value,
        onIntent = viewModel::onIntent,
        isCalendarExpanded = isCalendarExpanded.value,
        onDayClick = onDayClick
    )
//    Log.d("calendar date", dateList.toString())
}

@Composable
private fun SmeemCalendarImpl(
    dateList: Array<List<Date>>,
    selectedDate: LocalDate,
    currentMonth: YearMonth,
    onIntent: (CalendarIntent) -> Unit,
    isCalendarExpanded: Boolean,
    onDayClick: (LocalDate) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.animateContentSize()
    ) {
        CalendarTitle(
            selectedMonth = currentMonth,
            modifier = Modifier.padding(vertical = 18.dp)
        )
        WeekLabel()
        if (isCalendarExpanded) {
            MonthlyCalendar(
                dateList = dateList,
                selectedDate = selectedDate,
                currentMonth = currentMonth,
                loadDatesForMonth = { yearMonth ->
                    onIntent(
                        CalendarIntent.LoadNextDates(
                            startDate = yearMonth.atDay(1),
                            period = Period.MONTH
                        )
                    )
                },
                onDayClick = {
                    onIntent(CalendarIntent.SelectDate(it))
                    onDayClick(it)
                }
            )
        } else {
            WeeklyCalendar(
                dateList = dateList,
                selectedDate = selectedDate,
                loadNextWeek = { nextWeekDate -> onIntent(CalendarIntent.LoadNextDates(nextWeekDate)) },
                loadPrevWeek = { endWeekDate -> onIntent(CalendarIntent.LoadNextDates(endWeekDate.minusDays(1).getWeekStartDate())) },
                onDayClick = {
                    onIntent(CalendarIntent.SelectDate(it))
                    onDayClick(it)
                }
            )
        }
        CalendarToggleArea(
            isExpanded = isCalendarExpanded,
            expand = { onIntent(CalendarIntent.ExpandCalendar) },
            collapse = { onIntent(CalendarIntent.CollapseCalendar) }
        )
    }
}