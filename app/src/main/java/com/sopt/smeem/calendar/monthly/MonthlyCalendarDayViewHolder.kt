package com.sopt.smeem.calendar.monthly

import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sopt.smeem.calendar.model.MonthlyCalendarDay
import com.sopt.smeem.databinding.ViewMonthlyCalendarDayBinding

class MonthlyCalendarDayViewHolder(private val binding: ViewMonthlyCalendarDayBinding) :
    ViewHolder(binding.root) {

    private lateinit var dayData: MonthlyCalendarDay.DayMonthly

    fun onBind(data: MonthlyCalendarDay) {
        if (data is MonthlyCalendarDay.DayMonthly) {
            dayData = data
            binding.apply {
                ivToday.visibility =
                    if (DateUtils.isToday(data.date.time)) View.VISIBLE else View.GONE
                dayItem = data
                executePendingBindings()
            }
        }
    }

    fun smeemBind(data: MonthlyCalendarDay) {
        if (data is MonthlyCalendarDay.DayMonthly) {
            dayData = data
            binding.ivToday.visibility =
                if (DateUtils.isToday(data.date.time)) View.VISIBLE else View.GONE
            binding.dayItem = data
        }

    }
}