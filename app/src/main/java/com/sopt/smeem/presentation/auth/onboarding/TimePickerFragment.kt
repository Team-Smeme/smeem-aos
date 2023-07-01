package com.sopt.smeem.presentation.auth.onboarding

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private val defaultHour = 22
    private val defaultMinute = 0
    private val is24Hour: Boolean = false

    private val vm: OnBoardingVM by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = OnBoardingVM() as T
        })[OnBoardingVM::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = defaultHour
        val minute = defaultMinute

        return SmeemTimePickerDialog(activity, this, hour, minute, is24Hour)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        vm.selectedHour.value = hourOfDay
        vm.selectedMinute.value = minute
    }

}