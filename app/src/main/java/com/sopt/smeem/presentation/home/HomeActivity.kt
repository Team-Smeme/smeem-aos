package com.sopt.smeem.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityHomeBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.calendar.listener.OnWeeklyCalendarSwipeListener
import com.sopt.smeem.presentation.mypage.MyPageActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeActivity : BindingActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private var todayData = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    private var weeklyData = todayData

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView(weeklyData)
        setInitListener()
        moveToMyPage()
        observeData()
    }

    private fun moveToMyPage() {
        binding.ivMyPage.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }
    }

    private fun initView(day: String) {
        binding.tvTargetMonth.text = LocalDate.now().format(
            DateTimeFormatter.ofPattern(TARGET_MONTH_PATTERN),
        )
        homeViewModel.getDateDiary(day)
    }

    private fun setInitListener() {
        binding.weeklyCalendar.setOnWeeklyDayClickListener { view, date ->
            binding.tvTargetMonth.text =
                date.format(DateTimeFormatter.ofPattern(TARGET_MONTH_PATTERN))

            binding.btnWriteDiary.visibility =
                if (LocalDate.now().isEqual(date)) View.VISIBLE else View.GONE

            homeViewModel.getDateDiary(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        }

        binding.weeklyCalendar.setOnWeeklyCalendarSwipeListener(object :
            OnWeeklyCalendarSwipeListener {
            override fun onSwipe(mondayDate: LocalDate?) {
                mondayDate?.let {
                    // TODO 서버 갱신
                    setTargetMonthTitle()
                }
            }
        })
    }

    private fun setTargetMonthTitle() {
        binding.tvTargetMonth.text = binding.weeklyCalendar.mondayDate?.format(
            DateTimeFormatter.ofPattern(TARGET_MONTH_PATTERN),
        )
    }

    private fun observeData() {
        homeViewModel.responseDiaries.observe(this) {
            homeViewModel.getDiaries(
                start = binding.weeklyCalendar.mondayDate?.plusDays(-6)?.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                ) ?: "",
                end = binding.weeklyCalendar.mondayDate?.plusDays(6)?.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                ) ?: "",
            )
        }

        homeViewModel.responseDateDiary.observe(this) {
            if (it == null) {
                binding.clDiaryList.visibility = View.GONE
                binding.clNoDiary.visibility = View.VISIBLE
            } else {
                binding.clDiaryList.visibility = View.VISIBLE
                binding.clNoDiary.visibility = View.GONE
                binding.tvDiaryWritenTime.text = it.createdAt
                binding.tvDiary.text = it.content
            }
        }
    }

    companion object {
        const val TARGET_MONTH_PATTERN = "yyyy년 M월"
    }
}
