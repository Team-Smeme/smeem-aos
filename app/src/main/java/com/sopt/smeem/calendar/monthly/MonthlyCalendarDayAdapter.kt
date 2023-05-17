package com.sopt.smeem.calendar.monthly

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sopt.smeem.R
import com.sopt.smeem.calendar.model.CalendarType
import com.sopt.smeem.calendar.model.MonthlyCalendarDay
import com.sopt.smeem.databinding.ViewMonthlyCalendarDayBinding
import com.sopt.smeem.databinding.ViewMonthlyCalendarEmptyBinding
import com.sopt.smeem.util.isTheSameDay
import java.util.*

class MonthlyCalendarDayAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val calendarItems = mutableListOf<MonthlyCalendarDay>()

    private val smeemCountList = mutableListOf<Pair<Date?, Int>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CalendarType.DAY.ordinal -> {
                val binding: ViewMonthlyCalendarDayBinding = DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.view_monthly_calendar_day,
                    parent,
                    false
                )
                MonthlyCalendarDayViewHolder(binding)
            }
            CalendarType.WEEK.ordinal -> {
                val binding: ViewMonthlyCalendarEmptyBinding = DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.view_monthly_calendar_empty,
                    parent,
                    false
                )
                MonthlyCalendarEmptyViewHolder(binding)
            }
            else -> {
                val binding: ViewMonthlyCalendarEmptyBinding = DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.view_monthly_calendar_empty,
                    parent,
                    false
                )
                MonthlyCalendarEmptyViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (calendarItems[position]) {
            is MonthlyCalendarDay.DayMonthly -> {
                smeemCountList.indexOfLast {
                    it.first?.isTheSameDay((calendarItems[position] as MonthlyCalendarDay.DayMonthly).date) == true
                }.also {
                    if (it != -1) {
                        (holder as MonthlyCalendarDayViewHolder).smeemBind(calendarItems[position])
                    } else {
                        (holder as MonthlyCalendarDayViewHolder).onBind(calendarItems[position])
                    }
                }
            }
            is MonthlyCalendarDay.Empty -> {
                (holder as MonthlyCalendarEmptyViewHolder).onBind(calendarItems[position])
            }

            MonthlyCalendarDay.Week -> {
                (holder as MonthlyCalendarEmptyViewHolder).onBind(calendarItems[position])
            }
        }

    }

    override fun getItemCount(): Int = calendarItems.size

    override fun getItemViewType(position: Int): Int = calendarItems[position].calendarType

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<MonthlyCalendarDay>) {
        calendarItems.clear()
        calendarItems.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitNotTodoCountList(list: List<Pair<Date?, Int>>) {
        smeemCountList.clear()
        smeemCountList.addAll(list)
        notifyDataSetChanged()
    }

}
