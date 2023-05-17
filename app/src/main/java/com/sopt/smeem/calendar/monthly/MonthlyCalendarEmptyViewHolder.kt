package com.sopt.smeem.calendar.monthly

import androidx.recyclerview.widget.RecyclerView
import com.sopt.smeem.calendar.model.MonthlyCalendarDay
import com.sopt.smeem.databinding.ViewMonthlyCalendarEmptyBinding

class MonthlyCalendarEmptyViewHolder(
    private val binding: ViewMonthlyCalendarEmptyBinding
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var empty: MonthlyCalendarDay.Empty

    fun onBind(data: MonthlyCalendarDay) {
        if (data is MonthlyCalendarDay.Empty) {
            empty = data
            binding.apply {

            }
        }
    }

}
