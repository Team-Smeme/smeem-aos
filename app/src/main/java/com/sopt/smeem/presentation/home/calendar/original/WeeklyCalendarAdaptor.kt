package com.sopt.smeem.presentation.home.calendar.original

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ViewWeeklyCalendarDayBinding
import com.sopt.smeem.domain.model.DiarySummary
import com.sopt.smeem.presentation.home.calendar.original.listener.OnWeeklyDayClickListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeeklyAdapter(private val onWeeklyDayClickListener: OnWeeklyDayClickListener) :
    RecyclerView.Adapter<WeeklyCalendarViewHolder>() {

    private val weeklyDays = mutableListOf<LocalDate>()
    private var selectedDay: LocalDate = LocalDate.now()
    private val diaryEntries = mutableListOf<DiarySummary>()

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
        val dateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val foundEntry = diaryEntries.any { entry ->
            runCatching {
                val entryLocalDate =
                    LocalDate.parse(entry.createdAt.substring(0, 10), dateTimePattern)
                entryLocalDate.isEqual(weeklyDays[position])
            }.getOrDefault(false)
        }
        if (selectedDay.isEqual(weeklyDays[position])) {
            holder.onSelectBind(weeklyDays[position], foundEntry)
        } else {
            holder.onBind(weeklyDays[position], foundEntry)
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
    
    fun setDiaryEntries(entries: List<DiarySummary>) {
        diaryEntries.clear()
        diaryEntries.addAll(entries)
    }

    companion object {
        private const val WEEKLY_CALENDAR_START_POSITION = 0
        private const val WEEKLY_CALENDAR_END_POSITION = 7
    }
}
