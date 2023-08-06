package com.sopt.smeem.presentation.onboarding

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private val defaultHour = 22
    private val defaultMinute = 0
    private val is24Hour: Boolean = false

    private val vm: OnBoardingVM by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = defaultHour
        val minute = defaultMinute

        val timePicker: TimePickerDialog = SmeemTimePickerDialog(activity, this, hour, minute, is24Hour)
        timePicker.setButton(DialogInterface.BUTTON_POSITIVE, "저장", timePicker)

        return timePicker
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        vm.selectedHour.value = hourOfDay
        vm.selectedMinute.value = minute
    }

}