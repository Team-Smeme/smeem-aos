package com.sopt.smeem.presentation.calendar

import android.graphics.Color
import android.text.format.DateUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sopt.smeem.R
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
                tvWeeklyCalendarDate.setTextColor(Color.parseColor("#FFFFFF"))
                return
            } else {
                ivToday.visibility = View.GONE
                tvWeeklyCalendarDate.setTextColor(Color.parseColor("#B8B8B8"))
            }

            tvWeeklyCalendarDate.setBackgroundResource(R.drawable.bg_diary_unselected_date)
            tvWeeklyCalendarDate.setTextColor(Color.parseColor("#B8B8B8"))
        }
    }

    fun onSelectBind(weeklyDate: LocalDate) {
        val localDate: Date = Date.valueOf(weeklyDate.toString()) as Date

        with(binding) {
            date = weeklyDate.dayOfMonth.toString()
            if (DateUtils.isToday(localDate.time)) {
                ivToday.visibility = View.VISIBLE
                tvWeeklyCalendarDate.setTextColor(Color.parseColor("#FFFFFF"))
                return
            } else {
                ivToday.visibility = View.GONE
                tvWeeklyCalendarDate.setTextColor(Color.parseColor("#B8B8B8"))
            }

            tvWeeklyCalendarDate.setBackgroundResource(R.drawable.bg_diary_selected_date)
            tvWeeklyCalendarDate.setTextColor(Color.parseColor("#171716"))
        }
    }

    override fun onClick(view: View) {
        onWeeklyDayClickListener.onWeeklyDayClick(view, weeklyDate)
    }

    override fun onLongClick(p0: View?): Boolean = false
}
