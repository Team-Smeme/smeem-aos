package com.sopt.smeem.presentation.home.calendar.original

import android.graphics.Color
import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ViewWeeklyCalendarDayBinding
import com.sopt.smeem.presentation.home.calendar.original.listener.OnWeeklyDayClickListener
import java.sql.Date
import java.time.LocalDate

class WeeklyCalendarViewHolder(
    private val binding: ViewWeeklyCalendarDayBinding,
    private val onWeeklyDayClickListener: OnWeeklyDayClickListener,
) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

    private var weeklyDate: LocalDate = LocalDate.now()
    init {
        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
    }

    fun onBind(weeklyDate: LocalDate, hasDiaryEntry: Boolean) {
        val localDate: Date = Date.valueOf(weeklyDate.toString()) as Date
        this.weeklyDate = weeklyDate
        with(binding) {
            date = weeklyDate.dayOfMonth.toString()
            val color =
                if (hasDiaryEntry) Color.parseColor("#171716") else Color.parseColor("#B8B8B8")

            if (DateUtils.isToday(localDate.time)) {
                ivToday.visibility = View.VISIBLE
                tvWeeklyCalendarDate.setTextColor(Color.parseColor("#FFFFFF"))
                return
            } else {
                ivToday.visibility = View.GONE
                tvWeeklyCalendarDate.setTextColor(color)
            }

            tvWeeklyCalendarDate.setBackgroundResource(R.drawable.bg_diary_unselected_date)
        }
    }

    fun onSelectBind(weeklyDate: LocalDate, hasDiaryEntry: Boolean) {
        val localDate: Date = Date.valueOf(weeklyDate.toString()) as Date

        with(binding) {
            date = weeklyDate.dayOfMonth.toString()
            val color = Color.parseColor("#171716")

            if (DateUtils.isToday(localDate.time)) {
                ivToday.visibility = View.VISIBLE
                tvWeeklyCalendarDate.setTextColor(Color.parseColor("#FFFFFF"))
                return
            } else {
                ivToday.visibility = View.GONE
                tvWeeklyCalendarDate.setTextColor(color)
            }

            tvWeeklyCalendarDate.setBackgroundResource(R.drawable.bg_diary_selected_date)
        }
    }

    override fun onClick(view: View) {
        onWeeklyDayClickListener.onWeeklyDayClick(view, weeklyDate)
    }

    override fun onLongClick(p0: View?): Boolean = false
}
