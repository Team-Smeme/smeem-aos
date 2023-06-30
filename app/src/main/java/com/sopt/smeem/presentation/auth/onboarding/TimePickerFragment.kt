package com.sopt.smeem.presentation.auth.onboarding

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private val defaultHour = 22
    private val defaultMinute = 0

    //    private val minuteInterval = 30
    private val is24Hour: Boolean = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = defaultHour
        val minute = defaultMinute

        return SmeemTimePickerDialog(activity, this, hour, minute, is24Hour)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        // TODO : Not yet implemented
    }

//    // 30분 단위로 선택 가능하도록
//    fun TimePicker.setMinuteInterval() {
//        val minMinute = 0
//        val maxMinute = 60
//        try {
//            val classForIdObtain = Class.forName("com.android.internal.R\$id")
//            val minutePickerId = classForIdObtain.getField("minute").getInt(null)
//            (this.findViewById(minutePickerId) as NumberPicker).apply {
//                minValue = minMinute
//                maxValue = maxMinute / minuteInterval - 1
//                displayedValues = getDisplayedValue(maxMinute)
//            }
//        } catch (e: Exception) {
//            Log.e("TimePickerDialog", "cannot set time interval due to $e")
//        }
//    }
//
//    private fun getDisplayedValue(max: Int): Array<String> {
//        val minutesArray = ArrayList<String>()
//        for (i in 0 until max step minuteInterval) {
//            minutesArray.add("%02d".format(i))
//        }
//        return minutesArray.toArray(arrayOf(""))
//    }
}