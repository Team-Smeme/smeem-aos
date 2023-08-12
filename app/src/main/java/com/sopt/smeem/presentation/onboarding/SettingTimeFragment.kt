package com.sopt.smeem.presentation.onboarding

import android.app.TimePickerDialog
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.FragmentSettingTimeBinding
import com.sopt.smeem.presentation.BindingFragment
import com.sopt.smeem.util.ButtonUtil.switchOff
import com.sopt.smeem.util.ButtonUtil.switchOn
import com.sopt.smeem.util.setOnSingleClickListener

class SettingTimeFragment :
    BindingFragment<FragmentSettingTimeBinding>(R.layout.fragment_setting_time) {
    private var days: Map<Int, TextView>? = null
    private var timePickerDialog: TimePickerDialog? = null
    private val vm: OnBoardingVM by activityViewModels()

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
        // 기본값 월화수목금 으로 설정
        setInitialDays()
    }
    
    private fun setInitialDays() {
        days?.values?.forEach { day ->
            run {
                when(day.text.toString()) {
                    "토" -> {}
                    "일" -> {}
                    else -> {
                        day.switchOn()
                        vm.addDay(day.text.toString())
                    }
                }
            }
        }
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
                    checkSelectedDays()
                }
            }
        }
    }

    private fun checkSelectedDays() {
        vm.isDaysEmpty.value = vm.days.isEmpty()
    }

    private fun onTouchTime() {
        binding.layoutOnBoardingTimeTime.setOnSingleClickListener {
//            TimePickerFragment().show(parentFragmentManager, "timePicker")
            TempTimePickerFragment().show(parentFragmentManager, "timePicker")
        }
    }

    private fun onTouchLater() {
        binding.tvOnBoardingTimeLaterStatic.setOnSingleClickListener {
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