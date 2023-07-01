package com.sopt.smeem.presentation.auth.onboarding

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.NumberPicker
import android.widget.TimePicker
import java.lang.reflect.Field

class SmeemTimePickerDialog(
    context: Context?,
    listener: OnTimeSetListener,
    hourOfDay: Int,
    minute: Int,
    is24HourView: Boolean
) : TimePickerDialog(context, null, hourOfDay, minute, is24HourView) {

    private var timePicker: TimePicker? = null
    private val minuteInterval = 30
    private val timeSetListener: OnTimeSetListener = listener

    override fun updateTime(hourOfDay: Int, minuteOfHour: Int) {
        with(timePicker!!) {
            hour = hourOfDay
            minute = minuteOfHour / minuteInterval
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            BUTTON_POSITIVE -> timeSetListener.onTimeSet(
                timePicker, timePicker!!.hour, timePicker!!.minute * minuteInterval
            )

            BUTTON_NEGATIVE -> cancel()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setMinuteInterval()
    }

    // 30분 단위로 선택 가능하도록
    private fun setMinuteInterval() {
        val minMinute = 0
        val maxMinute = 60
        try {
            val classForIdObtain = Class.forName("com.android.internal.R\$id")
            val timePickerField: Field = classForIdObtain.getField("timePicker")
            val minutePickerField: Field = classForIdObtain.getField("minute")

            timePicker = findViewById(timePickerField.getInt(null))
            val minuteSpinner =
                timePicker!!.findViewById(minutePickerField.getInt(null)) as NumberPicker
            minuteSpinner.apply {
                minValue = minMinute
                maxValue = maxMinute / minuteInterval - 1
                displayedValues = getDisplayedValue(maxMinute)
            }
        } catch (e: Exception) {
            Log.e("TimePickerDialog", "cannot set time interval due to $e")
        }
    }

    private fun getDisplayedValue(max: Int): Array<String> {
        val minutesArray = ArrayList<String>()
        for (i in 0 until max step minuteInterval) {
            minutesArray.add("%02d".format(i))
        }
        return minutesArray.toArray(arrayOf(""))
    }
}