package com.sopt.smeem.presentation.mypage

import android.content.Intent
import android.content.res.ColorStateList
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityMyPageBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.splash.SplashLoginActivity
import com.sopt.smeem.util.ButtonUtil.switchOff
import com.sopt.smeem.util.ButtonUtil.switchOn
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageActivity : BindingActivity<ActivityMyPageBinding>(R.layout.activity_my_page) {
    private val vm: MyPageVM by viewModels()
    private var days: Map<Int, TextView>? = null

    override fun constructLayout() {
        binding.ivMyPageEncouragingToEdit.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.white, null))
        getFromServer()
        setPush()
        setUpDays()
        setData()
    }

    override fun addListeners() {
        onTouchSwitchingPush()
        onEditNickname()
        onTouchDays()
        onTouchBadge()
        onEditGoal()
        onTouchBack()
        onTouchMenu()
    }

    private fun onTouchBack() {
        binding.topbarMyPage.setNavigationOnClickListener {
            finish()
        }
    }

    private fun onTouchMenu() {
        binding.topbarMyPage.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menual -> {
                    Toast.makeText(this, "준비중입니다.", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.logout -> {
                    vm.clearLocal()
                    startActivity(Intent(this, SplashLoginActivity::class.java))
                    finishAffinity()
                    true
                }

                R.id.withdrawal -> {
                    vm.withdrawal()
                    startActivity(Intent(this, SplashLoginActivity::class.java))
                    finishAffinity()
                    true
                }

                else -> false

            }
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
            startActivity(Intent(this, EditTrainingGoalActivity::class.java))
            finish()
        }
    }

    private fun onTouchDays() {
        days?.values?.forEach { day ->
            run {
                day.setOnClickListener {
                    // 요일이 선택되어있는 경우
                    if (vm.isDaySelected(day.text.toString())) {
                        day.switchOff()
                        day.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.white, null))
                        vm.removeDay(day.text.toString())
                    }
                    // 요일이 선택되어져있지 않은 경우
                    else {
                        day.switchOn()
                        day.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.point, null))
                        vm.addDay(day.text.toString())
                    }
                }
            }
        }
    }

    private fun onEditNickname() {
        binding.ivMyPageEditNickname.setOnSingleClickListener {
            Intent(this, ChangingNicknameActivity::class.java).apply {
                putExtra("originalNickname", binding.tvMyPageNickname.text)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
            binding.tvMyPageNickname.text = it.username
            binding.trainingGoal = it.goal
            binding.latestBadge = it.badge

            if (it.hasPushAlarm) {
                unFreezeTimeTable()
                days?.values?.forEach { day ->
                    if (vm.isDaySelected(day.text.toString())) {
                        day.switchOn()
                    }
                }
            } else {
                freezeTimeTable()
            }
        }
    }

    private fun onTouchSwitchingPush() {
        binding.switchMyPageAlarm.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                unFreezeTimeTable()
            } else {
                freezeTimeTable()
            }
        }
    }

    private fun setPush() {
        binding.switchMyPageAlarm.isChecked = getPushPermission()

        if (getPushPermission()) {
            unFreezeTimeTable()
        } else {
            freezeTimeTable()
        }
    }

    private fun unFreezeTimeTable() {
        binding.switchMyPageAlarm.trackTintList =
            ColorStateList.valueOf(resources.getColor(R.color.point, null))
        binding.switchMyPageAlarm.trackDecorationTintList =
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
            ColorStateList.valueOf(resources.getColor(R.color.gray_300, null))
        binding.switchMyPageAlarm.trackDecorationTintList =
            ColorStateList.valueOf(resources.getColor(R.color.gray_300, null))

        cannotTouch()
        // 요일 얼리기
        days?.values?.forEach { day ->
            if (vm.isDaySelected(day.text.toString())) {
                day.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.gray_500, null))
                day.setTextColor(resources.getColor(R.color.gray_100, null))
            }
        }

        // 시간 얼리기
        binding.tvMyPageTimeBoxTitleStatic.setTextColor(resources.getColor(R.color.gray_500, null))
        binding.tvMyPageTimeBoxBody.setTextColor(resources.getColor(R.color.gray_500, null))
    }

    private fun getPushPermission(): Boolean {
        // TODO : dataStore? or System 으로 부터 push 허용 여부 전달
        return false
    }

    fun getFromServer() {
        vm.getData { t -> Toast.makeText(this, t.cause.toString(), Toast.LENGTH_SHORT).show() }
    }

    private fun cannotTouch() {
        days?.values?.forEach { day -> day.isEnabled = false }
        binding.tvMyPageTimeBoxTitleStatic.isEnabled = false
        binding.tvMyPageTimeBoxBody.isEnabled = false
    }

    private fun canTouch() {
        days?.values?.forEach { day -> day.isEnabled = true }
        binding.tvMyPageTimeBoxTitleStatic.isEnabled = true
        binding.tvMyPageTimeBoxBody.isEnabled = true
    }
}