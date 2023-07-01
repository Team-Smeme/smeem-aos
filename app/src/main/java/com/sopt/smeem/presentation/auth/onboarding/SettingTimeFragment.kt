package com.sopt.smeem.presentation.auth.onboarding

import android.app.TimePickerDialog
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sopt.smeem.R
import com.sopt.smeem.databinding.FragmentSettingTimeBinding
import com.sopt.smeem.presentation.BindingFragment
import com.sopt.smeem.util.ButtonUtil.switchOff
import com.sopt.smeem.util.ButtonUtil.switchOn

class SettingTimeFragment :
    BindingFragment<FragmentSettingTimeBinding>(R.layout.fragment_setting_time) {
    private var days: Map<Int, TextView>? = null
    private var timePickerDialog: TimePickerDialog? = null
    private val vm: OnBoardingVM by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = OnBoardingVM() as T
        })[OnBoardingVM::class.java]
    }

    override fun constructLayout() {
        setUpDays()
    }

    override fun addListeners() {
        onTouchDays()
        onTouchTime()
        onTouchLater()
    }

    override fun addObservers() {
        observeHour()
        observeMinute()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        days = null
        timePickerDialog = null
    }

    private fun setUpDays() {
        days = mapOf(
            binding.tvOnBoardingTimeMon.id to binding.tvOnBoardingTimeMon,
            binding.tvOnBoardingTimeTue.id to binding.tvOnBoardingTimeTue,
            binding.tvOnBoardingTimeWed.id to binding.tvOnBoardingTimeWed,
            binding.tvOnBoardingTimeThr.id to binding.tvOnBoardingTimeThr,
            binding.tvOnBoardingTimeFri.id to binding.tvOnBoardingTimeFri,
            binding.tvOnBoardingTimeSat.id to binding.tvOnBoardingTimeSat,
            binding.tvOnBoardingTimeSun.id to binding.tvOnBoardingTimeSun
        )
    }

    private fun onTouchDays() {
        days?.values?.forEach { day ->
            run {
                day.setOnClickListener {
                    // 요일이 선택되어있는 경우
                    if (vm.isDaySelected(day.text.toString())) {
                        day.switchOff()
                        vm.removeDay(day.text.toString())
                    }
                    // 요일이 선택되어져있지 않은 경우
                    else {
                        day.switchOn()
                        vm.addDay(day.text.toString())
                    }
                }
            }
        }
    }

    private fun onTouchTime() {
        binding.layoutOnBoardingTimeTime.setOnClickListener {
            TimePickerFragment().show(parentFragmentManager, "timePicker")
        }
    }

    private fun onTouchLater() {
        binding.tvOnBoardingTimeLaterStatic.setOnClickListener {
            vm.timeLater()
        }
    }

    private fun observeHour() {
        vm.selectedHour.observe(viewLifecycleOwner) {
            binding.tvOnBoardingTimeBodyHour.text = vm.formatHour(it)
            binding.tvOnBoardingTimeBodyAmpm.text = vm.getAmPm(it)
        }
    }

    private fun observeMinute() {
        vm.selectedMinute.observe(viewLifecycleOwner) {
            binding.tvOnBoardingTimeBodyMinute.text = vm.formatMinute(it)
        }
    }

}