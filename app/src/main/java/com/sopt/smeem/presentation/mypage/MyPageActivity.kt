package com.sopt.smeem.presentation.mypage

import android.content.Intent
import android.content.res.ColorStateList
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.sopt.smeem.Day
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityMyPageBinding
import com.sopt.smeem.domain.model.AlarmEnabledDay
import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.BadgeType
import com.sopt.smeem.domain.model.Language
import com.sopt.smeem.domain.model.OnBoardingGoal
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.ButtonUtil.switchOff
import com.sopt.smeem.util.ButtonUtil.switchOn

class MyPageActivity : BindingActivity<ActivityMyPageBinding>(R.layout.activity_my_page) {
    private val vm: MyPageVM by viewModels()
    private var days: Map<Int, TextView>? = null
    private lateinit var languageAdaptor: LanguagesAdaptor

    override fun constructLayout() {
        // TODO : 서버의 값으로만 치환할 수 있도록
        //  getFromServer()
        setAdaptor()
        setPush()
        setUpDays()
    }

    override fun addObservers() {
        // setData()
        mocking()
    }

    override fun addListeners() {
        onTouchSwitchingPush()
        onEditNickname()
        onTouchDays()
        onTouchBadge()
        // onEditGoal()
    }

    private fun setUpDays() {
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
        binding.ivMyPageEditNickname.setOnClickListener {
            startActivity(Intent(this, ChangingNicknameActivity::class.java))
        }
    }

    private fun onTouchBadge() {
        binding.layoutMyPageBadges.setOnClickListener {
            startActivity(Intent(this, MyBadgesActivity::class.java))
        }
    }
    private fun setData() { // TODO : mocking 대신 호출되어져야함
        vm.response.observe(this) {
            binding.tvMyPageNickname.text = it.username
            binding.onBoardingGoal = it.goal
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
            languageAdaptor.submitList(listOf(it.language))
        }
    }

    private fun setAdaptor() {
        languageAdaptor = LanguagesAdaptor()
        binding.rvMyPageLanguages.adapter = languageAdaptor
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

    // TODO : remove
    fun mocking() {
        binding.tvMyPageNickname.text = "정요니"
        binding.onBoardingGoal = OnBoardingGoal(
            "외국어 원서 읽기",
            "매일 매일 꼬박꼬박 읽으세요\n읽다보면 늘어나요",
            "외국어 원서 다 읽기\n영어 고득점 받기"
        )
        binding.latestBadge = Badge(badgeId = 1, title = "웰컴뱃지", description = "한 줄 설명 설명 환영합니다", badgeType = BadgeType.WELCOME)
        binding.alarmEnabledDay =
            AlarmEnabledDay(days = setOf(Day.SUN), hour = 11, minute = 10, ampm = "AM")
        languageAdaptor.submitList(listOf(Language(code = 1L)))

        freezeTimeTable()
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