package com.sopt.smeem.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.data.datasource.DummyDate.diaryDateList
import com.sopt.smeem.domain.model.Date
import com.sopt.smeem.domain.model.DiarySummaries
import com.sopt.smeem.domain.model.DiarySummary
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.presentation.home.calendar.core.CalendarIntent
import com.sopt.smeem.presentation.home.calendar.core.Period
import com.sopt.smeem.util.getNextDates
import com.sopt.smeem.util.getRemainingDatesInMonth
import com.sopt.smeem.util.getRemainingDatesInWeek
import com.sopt.smeem.util.getWeekStartDate
import com.sopt.smeem.util.toYearMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
) : ViewModel() {
    lateinit var nowBadge: String

    var isFirstBadge: Boolean = true
    val badgeName = MutableLiveData<String>()
    val badgeImageUrl = MutableLiveData<String>()

    private val _responseDiaries: MutableLiveData<DiarySummaries> = MutableLiveData()
    val responseDiaries: LiveData<DiarySummaries>
        get() = _responseDiaries

    private val _responseDateDiary: MutableLiveData<DiarySummary?> = MutableLiveData()
    val responseDateDiary: LiveData<DiarySummary?> get() = _responseDateDiary

    suspend fun getDiaries(start: String, end: String) {
        kotlin.runCatching {
            diaryRepository.getDiaries(start, end)
        }.fold({
            Log.e("message", it.getOrNull().toString())
            _responseDiaries.value = it.getOrNull()
        }, {
            Log.e("message", it.message.toString())
        })
    }

    suspend fun getDateDiary(date: String) {
        kotlin.runCatching {
            diaryRepository.getDiaries(start = date, end = date)
        }.fold({
            _responseDateDiary.value = it.getOrNull()?.diaries?.values?.firstOrNull()
        }, {
            Log.e("message", it.message.toString())
        })
    }

    fun setBadgeInfo(name: String, imageUrl: String, isFirst: Boolean) {
        badgeName.value = name
        badgeImageUrl.value = imageUrl
        isFirstBadge = isFirst

        nowBadge = name
    }

    /******* calendar related *******/
    private val _visibleDates =
        MutableStateFlow(
            calculateWeeklyCalendarDays(
                startDate = LocalDate.now().getWeekStartDate().minusWeeks(1)
            )
        )
    val visibleDates: StateFlow<Array<List<Date>>> = _visibleDates

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    val currentMonth: StateFlow<YearMonth>
        get() = isCalendarExpanded.zip(visibleDates) { isExpanded, dates ->
            when {
                isExpanded -> dates[1][dates[1].size / 2].day.toYearMonth()
                dates[1].count { it.day.month == dates[1][0].day.month } > 3 -> dates[1][0].day.toYearMonth()
                else -> dates[1][dates[1].size - 1].day.toYearMonth()
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, LocalDate.now().toYearMonth())

    private val _isCalendarExpanded = MutableStateFlow(false)
    val isCalendarExpanded: StateFlow<Boolean> = _isCalendarExpanded

    fun onIntent(intent: CalendarIntent) {
        when (intent) {
            CalendarIntent.ExpandCalendar -> {
                calculateCalendarDates(
                    startDate = currentMonth.value.minusMonths(1).atDay(1),
                    period = Period.MONTH
                )
                _isCalendarExpanded.value = true
            }

            CalendarIntent.CollapseCalendar -> {
                calculateCalendarDates(
                    startDate = calculateWeeklyCalendarVisibleStartDay()
                        .getWeekStartDate()
                        .minusWeeks(1),
                    period = Period.WEEK
                )
                _isCalendarExpanded.value = false
            }

            is CalendarIntent.LoadNextDates -> {
                calculateCalendarDates(intent.startDate, intent.period)
            }

            is CalendarIntent.SelectDate -> {
                viewModelScope.launch {
                    _selectedDate.emit(intent.date)
                }
            }
        }
    }

    private fun calculateCalendarDates(
        startDate: LocalDate,
        period: Period = Period.WEEK
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _visibleDates.emit(
                when (period) {
                    Period.WEEK -> calculateWeeklyCalendarDays(startDate)
                    Period.MONTH -> calculateMonthlyCalendarDays(startDate)
                }
            )
        }
    }

    private fun calculateWeeklyCalendarVisibleStartDay(): LocalDate {
        val halfOfMonth = visibleDates.value[1][visibleDates.value[1].size / 2]
        val visibleMonth = YearMonth.of(halfOfMonth.day.year, halfOfMonth.day.month)
        return if (selectedDate.value.month == visibleMonth.month && selectedDate.value.year == visibleMonth.year)
            selectedDate.value
        else visibleMonth.atDay(1)
    }

    private fun calculateWeeklyCalendarDays(startDate: LocalDate): Array<List<Date>> {
        val dateList = mutableListOf<Date>()
        startDate.getNextDates(21).map {
            dateList.add(Date(it, true, it in diaryDateList.value))
        }
        return Array(3) {
            dateList.slice(it * 7 until (it + 1) * 7)
        }
    }

    private fun calculateMonthlyCalendarDays(startDate: LocalDate): Array<List<Date>> {
        return Array(3) { monthIndex ->
            val monthFirstDate = startDate.plusMonths(monthIndex.toLong())
            val monthLastDate = monthFirstDate.plusMonths(1).minusDays(1)

            monthFirstDate.getWeekStartDate().let { weekBeginningDate ->
                if (weekBeginningDate != monthFirstDate) {
                    weekBeginningDate.getRemainingDatesInMonth().map {
                        Date(it, false, it in diaryDateList.value)
                    }
                } else {
                    listOf()
                } +
                        monthFirstDate.getNextDates(monthFirstDate.month.length(monthFirstDate.isLeapYear))
                            .map {
                                Date(it, true, it in diaryDateList.value)
                            } +
                        monthLastDate.getRemainingDatesInWeek().map {
                            Date(it, false, it in diaryDateList.value)
                        }
            }
        }
    }
}
