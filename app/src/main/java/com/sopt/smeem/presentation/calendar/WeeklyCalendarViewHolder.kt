package com.sopt.smeem.presentation.calendar

import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sopt.smeem.databinding.ViewWeeklyCalendarDayBinding
import com.sopt.smeem.presentation.calendar.listener.OnWeeklyDayClickListener
import java.sql.Date
import java.time.LocalDate

class WeeklyCalendarViewHolder(
    private val binding: ViewWeeklyCalendarDayBinding,
    private val onWeeklyDayClickListener: OnWeeklyDayClickListener,
) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

    private lateinit var weeklyDate: LocalDate

    init {
        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
    }

    fun onBind(weeklyDate: LocalDate) {
        val localDate: Date = Date.valueOf(weeklyDate.toString()) as Date
        this.weeklyDate = weeklyDate
        with(binding) {
            date = weeklyDate.dayOfMonth.toString()
            if (DateUtils.isToday(localDate.time)) {
                ivToday.visibility = View.VISIBLE
                tvWeeklyCalendarDate.setTextColor(android.graphics.Color.parseColor("#FFFFFF"))
            } else {
                ivToday.visibility = View.GONE
                tvWeeklyCalendarDate.setTextColor(android.graphics.Color.parseColor("#B8B8B8"))
            }
        }
    }

    override fun onClick(view: View) {
        onWeeklyDayClickListener.onWeeklyDayClick(view, weeklyDate)
    }

    override fun onLongClick(p0: View?): Boolean = false
}
