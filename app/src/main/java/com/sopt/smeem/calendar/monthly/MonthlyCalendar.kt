package com.sopt.smeem.calendar.monthly

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.sopt.smeem.R
import com.sopt.smeem.calendar.MonthlyCalendarNextMonthListener
import com.sopt.smeem.calendar.MonthlyCalendarPrevMonthListener
import com.sopt.smeem.calendar.NoRippleRecyclerView
import com.sopt.smeem.calendar.model.DAY_COLUMN_COUNT
import com.sopt.smeem.calendar.model.DateType
import com.sopt.smeem.calendar.model.MonthlyCalendarDay
import com.sopt.smeem.calendar.model.TOTAL_COLUMN_COUNT
import com.sopt.smeem.databinding.ViewCalendarWeekDescriptonBinding
import com.sopt.smeem.dpToPx
import com.sopt.smeem.util.isWeekend
import com.sopt.smeem.util.toPrettyDateString
import com.sopt.smeem.util.toPrettyMonthString
import java.util.*

class MonthlyCalendar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private var monthlyCalendarNextMonthListener: MonthlyCalendarNextMonthListener? = null
    private var monthlyCalendarPrevMonthListener: MonthlyCalendarPrevMonthListener? = null

    private val timeZone = TimeZone.getDefault()
    private val locale = Locale.KOREA
    private val monthlyCalendarDayAdapter = MonthlyCalendarDayAdapter()
    val calendar = Calendar.getInstance(timeZone, locale)
    private var calendarDataList: List<MonthlyCalendarDay> = listOf()
    private var currentDate = calendar.toPrettyMonthString(locale = locale)
        set(value) {
            field = value
            updateCurrentDateTextView()
        }

    private val currentDateTextView = TextView(context, null, R.style.B14).apply {
        id = ViewCompat.generateViewId()
        text = currentDate
        layoutParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        gravity = Gravity.CENTER
//        typeface = ResourcesCompat.getFont(context,R.font.pretendard_semibold)
//        setTextColor(ContextCompat.getColor(context, R.color.black_2a292d))
//        setTextSize(TypedValue.COMPLEX_UNIT_DIP,14f)
//        setBackgroundResource(R.drawable.bg_monthly_calendar_current_month)
    }

    private val calendarHeaderLinearLayout = LinearLayout(context).apply {
        id = ViewCompat.generateViewId()
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        setPadding(0, context.dpToPx(24), 0, context.dpToPx(24))

        addView(currentDateTextView)

    }

    private val calendarWeekDescriptionView = ViewCalendarWeekDescriptonBinding.inflate(
        LayoutInflater.from(context), this, false
    )

    private val monthRecyclerView = NoRippleRecyclerView(context).apply {
        id = ViewCompat.generateViewId()
        adapter = monthlyCalendarDayAdapter
        layoutManager = GridLayoutManager(context, TOTAL_COLUMN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return DAY_COLUMN_COUNT
                }
            }
        }
        layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        overScrollMode = OVER_SCROLL_NEVER
        setHasFixedSize(true)
    }

    init {
        if (attrs != null) {
            getStyleableAttrs(attrs)
        }
//        updateBorder()
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = LinearLayout.VERTICAL

        addView(calendarHeaderLinearLayout)
        addView(calendarWeekDescriptionView.root)
        addView(monthRecyclerView)
        initBackgroundColor()
        initializeSmeemMonthCalendar()
    }


    private fun getStyleableAttrs(attrs: AttributeSet) {

    }

    private fun initBackgroundColor() {
        setBackgroundColor(Color.parseColor("#ffffff"))
    }

    private fun initializeSmeemMonthCalendar() {
        initCalendarData()
    }

    private fun initCalendarData() {
        calendarDataList = buildCalendarData()
        monthlyCalendarDayAdapter.submitList(calendarDataList)
    }

    private fun updateCurrentDateTextView() {
        currentDateTextView.text = currentDate
    }

    private fun createStartEmptyView(
        dayOfWeek: Int,
        proxyCalendar: Calendar
    ): List<MonthlyCalendarDay.Empty> {
        val previousCalendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, proxyCalendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, proxyCalendar.get(Calendar.DAY_OF_MONTH))
            set(Calendar.YEAR, proxyCalendar.get(Calendar.YEAR))
        }.also {
            it.add(Calendar.MONTH, -1)
        }
        val numberOfEmptyView = when (dayOfWeek) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            Calendar.SATURDAY -> 6
            else -> 0
        }
        var startDayInPreviousMonth =
            previousCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - numberOfEmptyView + 1
        val listEmpty = mutableListOf<MonthlyCalendarDay.Empty>()
        repeat((0 until numberOfEmptyView).count()) {
            listEmpty.add(
                MonthlyCalendarDay.Empty(
                    startDayInPreviousMonth++.toString()
                )
            )
        }
        return listEmpty
    }

    private fun buildCalendarData(): List<MonthlyCalendarDay> {
        // 현재 달력이 보여주고 있는 Calendar instance의 복사본
        val proxyCalendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
        }

        val totalDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val monthlyCalendarDayList = mutableListOf<MonthlyCalendarDay>()
        (1..totalDayInMonth).forEach { day ->
            proxyCalendar.set(Calendar.DAY_OF_MONTH, day)
            val dayOfWeek = proxyCalendar.get(Calendar.DAY_OF_WEEK)
            val dateType = if (proxyCalendar.isWeekend()) {
                DateType.WEEKEND
            } else {
                DateType.WEEKDAY
            }

            when (day) {
                1 -> {
                    monthlyCalendarDayList.addAll(
                        createStartEmptyView(
                            dayOfWeek,
                            proxyCalendar
                        )
                    )
                    monthlyCalendarDayList.add(
                        MonthlyCalendarDay.DayMonthly(
                            day.toString(),
                            proxyCalendar.toPrettyDateString(),
                            proxyCalendar.time,
                            state = dateType
                        )
                    )
                }
                totalDayInMonth -> {
                    monthlyCalendarDayList.add(
                        MonthlyCalendarDay.DayMonthly(
                            day.toString(),
                            proxyCalendar.toPrettyDateString(),
                            proxyCalendar.time,
                            state = dateType
                        )
                    )
                    monthlyCalendarDayList.addAll(
                        createEndEmptyView(
                            dayOfWeek,
                            proxyCalendar
                        )
                    )
                }
                else -> {
                    monthlyCalendarDayList.add(
                        MonthlyCalendarDay.DayMonthly(
                            day.toString(),
                            proxyCalendar.toPrettyDateString(),
                            proxyCalendar.time,
                            state = dateType
                        )
                    )
                }
            }
        }
        return monthlyCalendarDayList
    }

    private fun createEndEmptyView(
        dayOfWeek: Int,
        proxyCalendar: Calendar
    ): List<MonthlyCalendarDay.Empty> {
        val nextCalendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, proxyCalendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, proxyCalendar.get(Calendar.DAY_OF_MONTH))
            set(Calendar.YEAR, proxyCalendar.get(Calendar.YEAR))
        }.also {
            it.add(Calendar.MONTH, 1)
        }
        var totalDayInNextMonth = nextCalendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        val numberOfEmptyView = when (dayOfWeek) {
            Calendar.SUNDAY -> 6
            Calendar.MONDAY -> 5
            Calendar.TUESDAY -> 4
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 2
            Calendar.FRIDAY -> 1
            else -> 0
        }

        val listEmpty = mutableListOf<MonthlyCalendarDay.Empty>()
        repeat((0 until numberOfEmptyView).count()) {
            listEmpty.add(
                MonthlyCalendarDay.Empty(
                    totalDayInNextMonth++.toString()
                )
            )
        }
        return listEmpty
    }

    fun setSmeemCountList(list: List<Pair<Date?, Int>>) {
        monthlyCalendarDayAdapter.submitNotTodoCountList(list)
    }

//    fun setOnMonthlyCalendarNextMonthListener(monthlyCalendarNextMonthListener: MonthlyCalendarNextMonthListener) {
//        this.monthlyCalendarNextMonthListener = monthlyCalendarNextMonthListener
//    }
//
//    fun setOnMonthlyCalendarNextMonthListener(block: (view: View, dateString: String) -> Unit) {
//        this.monthlyCalendarNextMonthListener = MonthlyCalendarNextMonthListener(block)
//    }
//
//    fun setOnMonthlyCalendarPrevMonthListener(monthlyCalendarPrevMonthListener: MonthlyCalendarPrevMonthListener) {
//        this.monthlyCalendarPrevMonthListener = monthlyCalendarPrevMonthListener
//    }
//
//    fun setOnMonthlyCalendarPrevMonthListener(block: (view: View, dateString: String) -> Unit) {
//        this.monthlyCalendarPrevMonthListener = MonthlyCalendarPrevMonthListener(block)
//    }


}