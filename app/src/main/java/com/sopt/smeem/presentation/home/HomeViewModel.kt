package com.sopt.smeem.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Smeem
import com.sopt.smeem.domain.model.Date
import com.sopt.smeem.domain.model.DiarySummary
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.event.AmplitudeEventType
import com.sopt.smeem.presentation.home.calendar.core.CalendarState
import com.sopt.smeem.presentation.home.calendar.core.Period
import com.sopt.smeem.util.DateUtil
import com.sopt.smeem.util.getNextDates
import com.sopt.smeem.util.getRemainingDatesInMonth
import com.sopt.smeem.util.getRemainingDatesInWeek
import com.sopt.smeem.util.getWeekStartDate
import com.sopt.smeem.util.toYearMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
) : ViewModel() {

    /***** variables *****/

    // badge
    var isFirstBadge: Boolean = true
    val badgeName = MutableLiveData<String>()
    val badgeImageUrl = MutableLiveData<String>()

    // diary
    private val _diaryDateList: MutableStateFlow<List<LocalDate>> = MutableStateFlow(emptyList())

    private val _diaryList: MutableLiveData<DiarySummary?> = MutableLiveData()
    val diaryList: LiveData<DiarySummary?>
        get() = _diaryList

    // calendar
    private val _visibleDates =
        MutableStateFlow(
            calculateWeeklyCalendarDays(
                startDate = LocalDate.now().getWeekStartDate().minusWeeks(1), emptyList()
            ),
        )
    val visibleDates: StateFlow<Array<List<Date>>> = _visibleDates

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    val currentMonth: StateFlow<YearMonth>
        get() = isCalendarExpanded.zip(visibleDates) { isExpanded, dates ->
            when {
                isExpanded -> dates[PRESENT][dates[PRESENT].size / 2].day.toYearMonth()
                dates[PRESENT].count { it.day.month == dates[PRESENT][FIRST_IN_ARRAY].day.month } > 3 -> dates[PRESENT][FIRST_IN_ARRAY].day.toYearMonth()
                else -> dates[PRESENT][dates[PRESENT].size - 1].day.toYearMonth()
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, LocalDate.now().toYearMonth())

    private val _isCalendarExpanded = MutableStateFlow(false)
    val isCalendarExpanded: StateFlow<Boolean> = _isCalendarExpanded

    /***** functions *****/

    // diary
    private suspend fun getDates(startDate: LocalDate, period: Period): List<LocalDate> {
        val endDate = when (period) {
            Period.WEEK -> startDate.plusDays(END_DATE_AFTER_THREE_WEEKS)
            Period.MONTH -> startDate.plusMonths(START_DATE_AFTER_THREE_MONTHS).minusDays(1)
        }
        val startAsString = DateUtil.WithServer.asStringOnlyDate(startDate)
        val endAsString = DateUtil.WithServer.asStringOnlyDate(endDate)

        return viewModelScope.async {
            kotlin.runCatching {
                diaryRepository.getDiaries(startAsString, endAsString)
            }.fold({
                it.getOrNull()?.diaries?.keys?.toList() ?: emptyList()
            }, {
                Timber.e(it.message.toString())
                emptyList()
            })
        }.await()
    }

    private suspend fun getDateDiary(date: LocalDate) {
        val dateAsString = DateUtil.WithServer.asStringOnlyDate(date)
        kotlin.runCatching {
            diaryRepository.getDiaries(start = dateAsString, end = dateAsString)
        }.fold({
            _diaryList.postValue(
                it.getOrNull()?.diaries?.values?.firstOrNull(),
            )
        }, {
            Timber.e(it.message.toString())
        })
    }

    fun setBadgeInfo(name: String, imageUrl: String, isFirst: Boolean) {
        badgeName.value = name
        badgeImageUrl.value = imageUrl
        isFirstBadge = isFirst
    }

    // calendar
    fun onStateChange(state: CalendarState) {
        when (state) {
            CalendarState.ExpandCalendar -> {
                calculateCalendarDates(
                    startDate = currentMonth.value.minusMonths(1).atDay(FIRST),
                    period = Period.MONTH,
                )
                _isCalendarExpanded.value = true
                sendEvent(AmplitudeEventType.FULL_CALENDAR_APPEAR)
            }

            CalendarState.CollapseCalendar -> {
                calculateCalendarDates(
                    startDate = calculateWeeklyCalendarVisibleStartDay()
                        .getWeekStartDate()
                        .minusWeeks(1),
                    period = Period.WEEK,
                )
                _isCalendarExpanded.value = false
            }

            is CalendarState.LoadNextDates -> {
                calculateCalendarDates(state.startDate, state.period)
            }

            is CalendarState.SelectDate -> {
                viewModelScope.launch {
                    getDateDiary(state.date)
                    _selectedDate.emit(state.date)
                }
            }
        }
    }

    private fun calculateCalendarDates(
        startDate: LocalDate,
        period: Period = Period.WEEK,
    ) {
        viewModelScope.launch {
            val diaryDateList = when (period) {
                Period.WEEK -> getDates(startDate, Period.WEEK)
                Period.MONTH -> getDates(startDate, Period.MONTH)
            }

            val visibleDates = when (period) {
                Period.WEEK -> calculateWeeklyCalendarDays(startDate, diaryDateList)
                Period.MONTH -> calculateMonthlyCalendarDays(startDate, diaryDateList)
            }

            withContext(Dispatchers.Main) {
                _diaryDateList.emit(diaryDateList)
                _visibleDates.emit(visibleDates)
            }

        }
    }

    private fun calculateWeeklyCalendarVisibleStartDay(): LocalDate {
        val halfOfMonth = visibleDates.value[PRESENT][visibleDates.value[PRESENT].size / 2]
        val visibleMonth = YearMonth.of(halfOfMonth.day.year, halfOfMonth.day.month)
        return if (selectedDate.value.month == visibleMonth.month && selectedDate.value.year == visibleMonth.year) {
            selectedDate.value
        } else {
            visibleMonth.atDay(FIRST)
        }
    }

    private fun calculateWeeklyCalendarDays(
        startDate: LocalDate,
        diaryDates: List<LocalDate>
    ): Array<List<Date>> {
        val dateList = mutableListOf<Date>()

        startDate.getNextDates(THREE_WEEKS).forEach {
            dateList.add(Date(it, true, diaryDates.contains(it)))
        }

        return Array(DATELIST_SIZE) {
            dateList.slice(it * 7 until (it + 1) * 7)
        }
    }

    private fun calculateMonthlyCalendarDays(
        startDate: LocalDate,
        diaryDates: List<LocalDate>
    ): Array<List<Date>> {
        return Array(DATELIST_SIZE) { monthIndex ->
            val monthFirstDate = startDate.plusMonths(monthIndex.toLong())
            val monthLastDate = monthFirstDate.plusMonths(1).minusDays(1)

            monthFirstDate.getWeekStartDate().let { weekBeginningDate ->
                if (weekBeginningDate != monthFirstDate) {
                    weekBeginningDate.getRemainingDatesInMonth().map {
                        Date(it, false, diaryDates.contains(it))
                    }
                } else {
                    listOf()
                } +
                        monthFirstDate.getNextDates(monthFirstDate.month.length(monthFirstDate.isLeapYear))
                            .map {
                                Date(it, true, diaryDates.contains(it))
                            } +
                        monthLastDate.getRemainingDatesInWeek().map {
                            Date(it, false, diaryDates.contains(it))
                        }
            }
        }
    }

    private fun sendEvent(event: AmplitudeEventType) {
        try {
            viewModelScope.launch {
                Smeem.AMPLITUDE.track(event.eventName)
            }
        } catch (t: Throwable) {
            // 이벤트 발송이 기존 로직에 영향은 없도록
            Timber.tag("AMPLITUDE").e("amplitude send error!")
        }
    }

    companion object {
        const val PRESENT = 1
        const val FIRST_IN_ARRAY = 0
        const val FIRST = 1
        const val END_DATE_AFTER_THREE_WEEKS: Long = 20
        const val START_DATE_AFTER_THREE_MONTHS: Long = 3
        const val THREE_WEEKS = 21
        const val DATELIST_SIZE = 3
    }
}
