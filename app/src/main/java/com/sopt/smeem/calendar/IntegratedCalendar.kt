package com.sopt.smeem.calendar

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.sopt.smeem.R
import com.sopt.smeem.calendar.monthly.MonthlyCalendar
import com.sopt.smeem.calendar.weekly.WeeklyCalendar

class IntegratedCalendar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {
    private lateinit var monthlyCalendar: MonthlyCalendar
    private lateinit var weeklyCalendar: WeeklyCalendar

    init {
        orientation = VERTICAL

        // Initialize MonthlyCalendar
        monthlyCalendar =
            MonthlyCalendar(context, attrs, defStyle = R.style.TopSheet_Background).apply {
                // Configure its properties here, e.g.
                // monthlyCalendarNextMonthListener = ...
                // monthlyCalendarPrevMonthListener = ...
            }
        val layoutParamsMonthly = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        // 상단에서 150dp만큼 마진 추가
        val margin = (150 * resources.displayMetrics.density).toInt()
        layoutParamsMonthly.setMargins(0, margin, 0, 0)
        monthlyCalendar.layoutParams = layoutParamsMonthly
        addView(monthlyCalendar)

        // Initialize WeeklyCalendar
        weeklyCalendar =
            WeeklyCalendar(context, attrs, defStyle = R.style.TopSheet_Background).apply {
                // Configure its properties here, e.g.
                // onWeeklyDayClickListener = ...
                // onWeeklyCalendarSwipeListener = ...
            }
        val layoutParamsWeekly = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        weeklyCalendar.layoutParams = layoutParamsWeekly
        addView(weeklyCalendar)
    }
}