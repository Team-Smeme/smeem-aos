package com.sopt.smeem.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityHomeBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.calendar.WritingBottomSheet
import com.sopt.smeem.presentation.calendar.WritingBottomSheet.Companion.TAG
import com.sopt.smeem.presentation.calendar.listener.OnWeeklyCalendarSwipeListener
import com.sopt.smeem.presentation.mypage.MyPageActivity
import com.sopt.smeem.util.DateUtil
import com.sopt.smeem.util.TextUtil.toLocalDateTime
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeActivity : BindingActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private var todayData = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    private var weeklyData = todayData
    lateinit var bs: WritingBottomSheet

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bs = WritingBottomSheet()
        initView(weeklyData)
        setInitListener()
        moveToMyPage()
        observeData()
        onTouchWrite()
    }

    private fun onTouchWrite() {
        binding.btnWriteDiary.setOnSingleClickListener {
            bs.show(supportFragmentManager, TAG)
        }
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
            with(binding) {
                if (it == null) {
                    clDiaryList.visibility = View.GONE
                    clNoDiary.visibility = View.VISIBLE
                } else {
                    clDiaryList.visibility = View.VISIBLE
                    clNoDiary.visibility = View.GONE
                    tvDiaryWritenTime.text = DateUtil.WithUser.hourMinute(it.createdAt.toLocalDateTime())
                    tvDiary.text = it.content
                }
            }
        }
    }

    companion object {
        const val TARGET_MONTH_PATTERN = "yyyy년 M월"
    }
}
