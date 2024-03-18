package com.sopt.smeem.presentation.home.calendar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sopt.smeem.domain.model.Date
import com.sopt.smeem.presentation.home.HomeViewModel
import com.sopt.smeem.presentation.home.calendar.component.CalendarTitle
import com.sopt.smeem.presentation.home.calendar.component.CalendarToggleSlider
import com.sopt.smeem.presentation.home.calendar.component.MonthlyCalendar
import com.sopt.smeem.presentation.home.calendar.component.WeekLabel
import com.sopt.smeem.presentation.home.calendar.component.WeeklyCalendar
import com.sopt.smeem.presentation.home.calendar.core.CalendarIntent
import com.sopt.smeem.presentation.home.calendar.core.Period
import com.sopt.smeem.presentation.home.calendar.ui.theme.gray100
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
        modifier = Modifier
            .animateContentSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    if (dragAmount.y < 0) {
                        onIntent(CalendarIntent.CollapseCalendar)
                    } else if (dragAmount.y > 0) {
                        onIntent(CalendarIntent.ExpandCalendar)
                    }
                }
            }
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
                loadPrevWeek = { endWeekDate ->
                    onIntent(
                        CalendarIntent.LoadNextDates(
                            endWeekDate.minusDays(
                                1
                            ).getWeekStartDate()
                        )
                    )
                },
                onDayClick = {
                    onIntent(CalendarIntent.SelectDate(it))
                    onDayClick(it)
                }
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    min = when {
                        isCalendarExpanded -> 24.dp
                        else -> 4.dp
                    }
                )
        )
        CalendarToggleSlider(
            modifier = Modifier.padding(vertical = 12.dp)
        )
        Divider(
            color = gray100,
            thickness = 4.dp
        )
    }
}