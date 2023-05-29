package com.sopt.smeem.presentation.auth.onboarding

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sopt.smeem.Day
import com.sopt.smeem.R
import com.sopt.smeem.databinding.FragmentSettingTimeBinding
import com.sopt.smeem.presentation.BindingFragment

class SettingTimeFragment :
    BindingFragment<FragmentSettingTimeBinding>(R.layout.fragment_setting_time) {
    private var days: Map<Int, TextView>? = null
    private var timePickerDialog: TimePickerDialog? = null
    private val vm: OnBoardingVM by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = OnBoardingVM() as T
        }).get(OnBoardingVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpTimePicker()
    }

    override fun constructLayout() {
        setUpDays()
    }

    override fun addListeners() {
        onTouchDays()
        onTouchTime()
        onTouchLater()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        days = null
        timePickerDialog = null
    }

    private fun setUpTimePicker() {/*
        val timePicker = TimePicker(context).apply {
            setIs24HourView(false)
        }

        timePickerDialog = TimePickerDialog(
            context,
            R.style.Widget_Smeem_TimePicker,
            object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    vm.hour = hourOfDay
                    vm.minute = minute
                }
            },
            13,
            30,
            false
        )

        timePickerDialog!!.setView(timePicker)*/

        val dialog = AlertDialog.Builder(requireContext()).create()
        val edialog: LayoutInflater = LayoutInflater.from(requireContext())
        // TODO : custom timepicker layout 구성 필요

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
                    val v = vm
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
            timePickerDialog?.show()
        }
    }

    private fun onTouchLater() {
        binding.tvOnBoardingTimeLaterStatic.setOnClickListener {
            vm.timeLater()
        }
    }

    private fun TextView.switchOff() {
        if (Day.from(this.text.toString()) == Day.MON) {
            this.setBackgroundResource(R.drawable.shape_time_day_inactive_left)
            this.setTextColor(resources.getColor(R.color.gray_500, null))
        } else if (Day.from(this.text.toString()) == Day.SUN) {
            this.setBackgroundResource(R.drawable.shape_time_day_inactive_right)
            this.setTextColor(resources.getColor(R.color.gray_500, null))
        } else {
            this.setBackgroundResource(R.drawable.shape_time_day_inactive)
            this.setTextColor(resources.getColor(R.color.gray_500, null))
        }
    }

    private fun TextView.switchOn() {
        if (Day.from(this.text.toString()) == Day.MON) {
            this.setBackgroundResource(R.drawable.shape_time_day_active_left)
            this.setTextColor(resources.getColor(R.color.white, null))
        } else if (Day.from(this.text.toString()) == Day.SUN) {
            this.setBackgroundResource(R.drawable.shape_time_day_active_right)
            this.setTextColor(resources.getColor(R.color.white, null))
        } else {
            this.setBackgroundColor(resources.getColor(R.color.point, null))
            this.setTextColor(resources.getColor(R.color.white, null))
        }
    }
}