package com.sopt.smeem.presentation.mypage

import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sopt.smeem.databinding.DialogTimePickerBinding
import com.sopt.smeem.presentation.mypage.EditTrainingTimeViewModel.Companion.DEFAULT_HOUR
import com.sopt.smeem.presentation.mypage.EditTrainingTimeViewModel.Companion.DEFAULT_MINUTE
import com.sopt.smeem.presentation.onboarding.OnBoardingVM

class EditTimePickerFragment : DialogFragment() {
    private val timePicker by lazy { DialogTimePickerBinding.inflate(layoutInflater) }
    private val vm: EditTrainingTimeViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = vm.hour?.value ?: DEFAULT_HOUR
        val minute = vm.minute?.value ?: DEFAULT_MINUTE

        val hourPicker = timePicker.npTimePickerHour
        val minutePicker = timePicker.npTimePickerMinute

        initHourPicker(hourPicker, hour)
        initMinutePicker(minutePicker, minute)

        return MaterialAlertDialogBuilder(requireContext())
            .setView(timePicker.root)
            .setNegativeButton("취소", null)
            .setPositiveButton("저장") { _, _ ->
                vm.hour.value = hourPicker.value
                vm.minute.value = minutePicker.value * MINUTE_INTERVAL
            }
            .create()
    }

    private fun initHourPicker(hp: NumberPicker, hour: Int) {
        hp.apply {
            minValue = 0
            maxValue = 23
            value = hour
            displayedValues = (0..23).map { "%02d".format(it) }.toTypedArray()
        }
    }

    private fun initMinutePicker(mp: NumberPicker, minute: Int) {
        mp.apply {
            minValue = 0
            maxValue = 1
            value = if (minute >= MINUTE_INTERVAL) 1 else 0
            displayedValues = arrayOf("00", "30")
        }
    }

    companion object {
        private const val MINUTE_INTERVAL = 30
    }
}