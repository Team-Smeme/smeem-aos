package com.sopt.smeem.presentation.mypage

import android.content.Intent
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityEditTrainingTimeBinding
import com.sopt.smeem.description
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.ButtonUtil.switchOff
import com.sopt.smeem.util.ButtonUtil.switchOn
import com.sopt.smeem.util.DateUtil
import com.sopt.smeem.util.getParcelable
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTrainingTimeActivity :
    BindingActivity<ActivityEditTrainingTimeBinding>(R.layout.activity_edit_training_time) {
    private val viewModel by viewModels<EditTrainingTimeViewModel>()
    private var days: Map<Int, TextView>? = null

    override fun constructLayout() {
        setUpDays()
        initData()
    }

    override fun addListeners() {
        onTouchBack()
        onTouchDays()
        onTouchTime()
        onTouchComplete()
    }

    override fun addObservers() {
        observeTime()
    }

    private fun setUpDays() {
        setDaysHeight()
        days = mapOf(
            binding.tvMyPageEditTimeMon.id to binding.tvMyPageEditTimeMon,
            binding.tvMyPageEditTimeTue.id to binding.tvMyPageEditTimeTue,
            binding.tvMyPageEditTimeWed.id to binding.tvMyPageEditTimeWed,
            binding.tvMyPageEditTimeThr.id to binding.tvMyPageEditTimeThr,
            binding.tvMyPageEditTimeFri.id to binding.tvMyPageEditTimeFri,
            binding.tvMyPageEditTimeSat.id to binding.tvMyPageEditTimeSat,
            binding.tvMyPageEditTimeSun.id to binding.tvMyPageEditTimeSun,
        )
    }

    private fun initData() {
        binding.trainingTime = intent.getParcelable("selectedTime", TrainingTime::class.java)
        viewModel.originalTime = binding.trainingTime!!

        viewModel.hour.value = binding.trainingTime!!.hour
        viewModel.minute.value = binding.trainingTime!!.minute
        viewModel.days.addAll(binding.trainingTime!!.days)
        days?.values?.forEach { day ->
            if (viewModel.isDaySelected(day.text.toString())) {
                day.switchOn()
            }
        }
    }

    private fun onTouchBack() {
        binding.topbarMyPageEditTime.setNavigationOnClickListener {
            finish()
        }
    }

    private fun onTouchDays() {
        days?.values?.forEach { day ->
            run {
                day.setOnClickListener {
                    // 요일이 선택되어있는 경우
                    if (viewModel.isDaySelected(day.text.toString())) {
                        day.switchOff()
                        viewModel.removeDay(day.text.toString())
                    }
                    // 요일이 선택되어져있지 않은 경우
                    else {
                        day.switchOn()
                        viewModel.addDay(day.text.toString())
                    }
                    checkSelection()
                }
            }
        }
    }

    private fun onTouchTime() {
        binding.layoutMyPageEditTimeTime.setOnSingleClickListener {
            EditTimePickerFragment().show(supportFragmentManager, "timePicker")
        }
    }

    private fun onTouchComplete() {
        binding.btnMyPageEditTime.setOnSingleClickListener {
            viewModel.sendServer { e ->
                Toast.makeText(applicationContext, e.description(), Toast.LENGTH_SHORT).show()
            }
            Intent(this, MyPageActivity::class.java).run {
                startActivity(this)
                finish()
            }
        }
    }

    private fun observeTime() {
        viewModel.hour.observe(this) {
            binding.tvMyPageEditTimeHour.text = "%02d".format(DateUtil.asHour(it))
            binding.tvMyPageEditTimeAmpm.text = " ${DateUtil.asAmpm(it)}"
            checkSelection()
        }
        viewModel.minute.observe(this) {
            binding.tvMyPageEditTimeMinute.text = DateUtil.asMinute(it)
            checkSelection()
        }
    }

    private fun setDaysHeight() {
        with(binding.flowMyPageEditTimeDays) {
            layoutParams.height = (getScreenWidth() - 36) / 7
            requestLayout()
        }
    }

    private fun getScreenWidth(): Int = resources.displayMetrics.widthPixels

    private fun checkSelection() {
        if (viewModel.canConfirmEdit()) completeButtonOn() else completeButtonOff()
    }

    private fun completeButtonOn() {
        with(binding.btnMyPageEditTime) {
            setBackgroundColor(resources.getColor(R.color.point, null))
            isEnabled = true
        }
    }

    private fun completeButtonOff() {
        with(binding.btnMyPageEditTime) {
            setBackgroundColor(resources.getColor(R.color.point_inactive, null))
            setTextColor(resources.getColor(R.color.white, null))
            isEnabled = false
        }
    }
}