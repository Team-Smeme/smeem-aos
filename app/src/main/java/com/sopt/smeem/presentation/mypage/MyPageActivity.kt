package com.sopt.smeem.presentation.mypage

import android.content.Intent
import android.content.res.ColorStateList
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.sopt.smeem.DefaultSnackBar
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityMyPageBinding
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.event.AmplitudeEventType
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.util.ButtonUtil.switchOn
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageActivity : BindingActivity<ActivityMyPageBinding>(R.layout.activity_my_page) {
    private val vm: MyPageVM by viewModels()
    private var days: Map<Int, TextView>? = null
    private lateinit var selectedTrainingTime: TrainingTime
    private val eventVm: EventVM by viewModels()

    override fun constructLayout() {
        binding.ivMyPageEncouragingToEdit.imageTintList =
            ColorStateList.valueOf(resources.getColor(R.color.white, null))
        getFromServer()
        setUpDays()
        setData()
        eventVm.sendEvent(AmplitudeEventType.MY_PAGE_VIEW)
    }

    override fun addListeners() {
        onTouchSwitchingPush()
        onEditNickname()
        onTouchBadge()
        onEditGoal()
        onTouchBack()
        onTouchMenu()
        onTouchTime()
    }

    private fun onTouchBack() {
        binding.btnMyPageBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun onTouchMenu() {
        binding.btnMyPageMenu.setOnSingleClickListener {
            startActivity(Intent(this, MyPageMoreActivity::class.java))
        }

    }

    private fun setUpDays() {
        setDaysHeight()
        days = mapOf(
            binding.tvMyPageTimeMon.id to binding.tvMyPageTimeMon,
            binding.tvMyPageTimeTue.id to binding.tvMyPageTimeTue,
            binding.tvMyPageTimeWed.id to binding.tvMyPageTimeWed,
            binding.tvMyPageTimeThr.id to binding.tvMyPageTimeThr,
            binding.tvMyPageTimeFri.id to binding.tvMyPageTimeFri,
            binding.tvMyPageTimeSat.id to binding.tvMyPageTimeSat,
            binding.tvMyPageTimeSun.id to binding.tvMyPageTimeSun,
        )
    }

    private fun setDaysHeight() {
        with(binding.flowMyPageTime) {
            layoutParams.height = (getScreenWidth() - 36) / 7
            requestLayout()
        }
    }

    private fun getScreenWidth(): Int = resources.displayMetrics.widthPixels

    private fun onEditGoal() {
        binding.layoutMyPageEncouraging.setOnClickListener {
            Intent(this, EditTrainingGoalActivity::class.java).apply {
                putExtra("originalGoal", vm.response.value!!.goal.goal)
            }.run(::startActivity)
        }
    }

    private fun onEditNickname() {
        binding.ivMyPageEditNickname.setOnSingleClickListener {
            Intent(this, ChangingNicknameActivity::class.java).apply {
                putExtra("originalNickname", binding.tvMyPageNickname.text)
            }.run(::startActivity)
        }
    }

    private fun onTouchBadge() {
        binding.layoutMyPageBadges.setOnClickListener {
            startActivity(Intent(this, MyBadgesActivity::class.java))
        }
    }

    private fun setData() {
        vm.response.observe(this) {
            selectedTrainingTime =
                if (it.trainingTime.isSet()) it.trainingTime
                else TrainingTime(
                    setOf(Day.MON, Day.TUE, Day.WED, Day.THU, Day.FRI),
                    22,
                    0
                )

            binding.tvMyPageNickname.text = it.username
            binding.trainingGoal = it.goal
            binding.latestBadge = it.myPageBadge
            binding.trainingTime = selectedTrainingTime
            vm.days.addAll(selectedTrainingTime.days)

            if (it.hasPushAlarm) {
                binding.switchMyPageAlarm.isChecked = true
                unFreezeTimeTable()
            } else {
                binding.switchMyPageAlarm.isChecked = false
                freezeTimeTable()
            }

            days?.values?.forEach { day ->
                if (vm.isDaySelected(day.text.toString())) {
                    day.switchOn()
                }
            }
        }
        showEditCompleted()
    }

    private fun showEditCompleted() {
        val msg = intent.getStringExtra("snackbarText")
        if (msg != null) {
            DefaultSnackBar.make(binding.root, msg).show()
        }
    }

    private fun onTouchTime() {
        binding.layoutMyPageAlarmTimeTable.setOnSingleClickListener {
            Intent(this, EditTrainingTimeActivity::class.java).apply {
                putExtra("selectedTime", selectedTrainingTime)
            }.run(::startActivity)
        }
    }

    private fun onTouchSwitchingPush() {
        binding.switchMyPageAlarm.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                vm.changePushAlarm(
                    hasAlarm = true,
                    onError = { e ->
                        Toast.makeText(this, e.errorCode.message, Toast.LENGTH_SHORT).show()
                    }
                )
                unFreezeTimeTable()
            } else {
                vm.changePushAlarm(
                    hasAlarm = false,
                    onError = { e ->
                        Toast.makeText(this, e.errorCode.message, Toast.LENGTH_SHORT).show()
                    }
                )
                freezeTimeTable()
            }
        }
    }

    private fun unFreezeTimeTable() {
        binding.switchMyPageAlarm.trackTintList =
            ColorStateList.valueOf(resources.getColor(R.color.point, null))

        canTouch()
        // 요일 녹이기
        days?.values?.forEach { day ->
            if (vm.isDaySelected(day.text.toString())) {
                day.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.point, null))
                day.setTextColor(resources.getColor(R.color.white, null))
            }
        }

        // 시간 녹이기
        binding.tvMyPageTimeBoxTitleStatic.setTextColor(resources.getColor(R.color.point, null))
        binding.tvMyPageTimeBoxBody.setTextColor(resources.getColor(R.color.point, null))
    }

    private fun freezeTimeTable() {
        binding.switchMyPageAlarm.trackTintList =
            ColorStateList.valueOf(resources.getColor(R.color.gray_200, null))

        cannotTouch()
        // 요일 얼리기
        days?.values?.forEach { day ->
            if (vm.isDaySelected(day.text.toString())) {
                day.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.gray_200, null))
                day.setTextColor(resources.getColor(R.color.white, null))
            }
        }

        // 시간 얼리기
        binding.tvMyPageTimeBoxTitleStatic.setTextColor(resources.getColor(R.color.gray_200, null))
        binding.tvMyPageTimeBoxBody.setTextColor(resources.getColor(R.color.gray_200, null))
    }

    fun getFromServer() {
        vm.getData { t -> Toast.makeText(this, t.cause.toString(), Toast.LENGTH_SHORT).show() }
    }

    private fun cannotTouch() {
        binding.layoutMyPageAlarmTimeTable.isClickable = false
    }

    private fun canTouch() {
        binding.layoutMyPageAlarmTimeTable.isClickable = true
    }
}