package com.sopt.smeem.calendar.weekly

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sopt.smeem.calendar.weekly.listener.OnWeeklyDayClickListener
import com.sopt.smeem.databinding.ViewWeeklyCalendarDayBinding
import com.sopt.smeem.util.dayNameParseToAlphabet
import java.sql.Date
import java.time.LocalDate

class WeeklyViewHolder(
    private val binding: ViewWeeklyCalendarDayBinding,
    private val onWeeklyDayClickListener: OnWeeklyDayClickListener
) :
    ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

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
            day = weeklyDate.dayOfWeek.name.dayNameParseToAlphabet()
        }
    }


    override fun onClick(view: View) {
        onWeeklyDayClickListener.onWeeklyDayClick(view, weeklyDate)
    }

    override fun onLongClick(p0: View?) : Boolean = false
}