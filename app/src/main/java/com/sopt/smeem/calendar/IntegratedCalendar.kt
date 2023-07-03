package com.sopt.smeem.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.sopt.smeem.R
import com.sopt.smeem.calendar.monthly.MonthlyCalendar
import com.sopt.smeem.calendar.weekly.WeeklyCalendar
import com.sopt.smeem.util.dp

class IntegratedCalendar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {
    private lateinit var monthlyCalendar: MonthlyCalendar
    private lateinit var weeklyCalendar: WeeklyCalendar
    private lateinit var handleView: View
    private lateinit var dividerView: View

    init {
        orientation = VERTICAL

        // Initialize MonthlyCalendar
        monthlyCalendar =
            MonthlyCalendar(context, attrs, defStyle = R.style.TopSheet_Background).apply {

            }
        val layoutParamsMonthly = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        val margin = 96.dp(context)
        layoutParamsMonthly.setMargins(0, margin, 0, 0)
        monthlyCalendar.layoutParams = layoutParamsMonthly
        addView(monthlyCalendar)

        // Initialize WeeklyCalendar
        weeklyCalendar =
            WeeklyCalendar(context, attrs, defStyle = R.style.TopSheet_Background).apply {

            }
        val layoutParamsWeekly = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        weeklyCalendar.layoutParams = layoutParamsWeekly
        addView(weeklyCalendar)

        handleView = View(context).apply {
            layoutParams = LayoutParams(
                78.dp(context),
                4.dp(context)
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            setBackgroundColor(ContextCompat.getColor(context, R.color.gray_300))
        }
        addView(handleView)

        dividerView = View(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                4.dp(context)
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = 12.dp(context)
            }
            setBackgroundColor(ContextCompat.getColor(context, R.color.gray_100))
        }
        addView(dividerView)
    }

    fun getWeeklyCalendar(): WeeklyCalendar {
        return weeklyCalendar
    }

}

