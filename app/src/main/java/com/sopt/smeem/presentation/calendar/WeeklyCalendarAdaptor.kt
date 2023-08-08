package com.sopt.smeem.presentation.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ViewWeeklyCalendarDayBinding
import com.sopt.smeem.presentation.calendar.listener.OnWeeklyDayClickListener
import java.time.LocalDate

class WeeklyAdapter(private val onWeeklyDayClickListener: OnWeeklyDayClickListener) :
    RecyclerView.Adapter<WeeklyCalendarViewHolder>() {

    private val weeklyDays = mutableListOf<LocalDate>()
    private var selectedDay: LocalDate = LocalDate.now()
    private var diaryEntries: Set<LocalDate> = setOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyCalendarViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewWeeklyCalendarDayBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.view_weekly_calendar_day,
            parent,
            false,
        )
        return WeeklyCalendarViewHolder(binding, onWeeklyDayClickListener)
    }

    override fun onBindViewHolder(holder: WeeklyCalendarViewHolder, position: Int) {
        if (selectedDay.isEqual(weeklyDays[position])) {
            holder.onSelectBind(weeklyDays[position], diaryEntries.contains(weeklyDays[position]))
        } else {
            holder.onBind(weeklyDays[position], diaryEntries.contains(weeklyDays[position]))
        }
    }

    override fun getItemCount() = weeklyDays.size

    fun submitList(list: List<LocalDate>) {
        weeklyDays.clear()
        weeklyDays.addAll(list)
        notifyItemRangeChanged(
            WEEKLY_CALENDAR_START_POSITION,
            WEEKLY_CALENDAR_END_POSITION,
        )
    }

    fun setSelectedDay(localDate: LocalDate) {
        val lastPosition = weeklyDays.indexOfLast { it == selectedDay }
        selectedDay = localDate
        if (lastPosition != -1) {
            notifyItemChanged(lastPosition)
        }
        val currentPosition = weeklyDays.indexOfLast { it == selectedDay }
        if (currentPosition != -1) {
            notifyItemChanged(currentPosition)
        }
    }

    companion object {
        private const val WEEKLY_CALENDAR_START_POSITION = 0
        private const val WEEKLY_CALENDAR_END_POSITION = 7
    }
}
