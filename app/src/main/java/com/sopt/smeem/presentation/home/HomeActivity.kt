package com.sopt.smeem.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityHomeBinding
import com.sopt.smeem.domain.model.RetrievedBadge
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.calendar.WritingBottomSheet
import com.sopt.smeem.presentation.calendar.WritingBottomSheet.Companion.TAG
import com.sopt.smeem.presentation.calendar.listener.OnWeeklyCalendarSwipeListener
import com.sopt.smeem.presentation.detail.DiaryDetailActivity
import com.sopt.smeem.presentation.mypage.MyPageActivity
import com.sopt.smeem.util.setOnSingleClickListener
import com.sopt.smeem.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            homeViewModel.getDateDiary(todayData)

            if (homeViewModel.responseDateDiary.value != null) {
                binding.btnWriteDiary.visibility = View.INVISIBLE
            }
        }
        getWeeklyDiary()
        observeData()
    }

    override fun onRestart() {
        super.onRestart()

        lifecycleScope.launch {
            homeViewModel.getDateDiary(todayData)

            if (homeViewModel.responseDateDiary.value != null) {
                binding.btnWriteDiary.visibility = View.INVISIBLE
            }
        }
        getWeeklyDiary()
        observeData()
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

        lifecycleScope.launch {
            homeViewModel.getDateDiary(day)

            binding.btnWriteDiary.visibility =
                if (homeViewModel.responseDateDiary.value == null) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
        }

        getWeeklyDiary()
        showDiaryCompleted()
        showBadgeDialog()
    }

    private fun showDiaryCompleted() {
        val msg = intent.getStringExtra("snackbarText")
        if (msg != null) {
            binding.root.showSnackbar(msg)
        }
    }

    private fun showBadgeDialog() {
        val retrievedBadge =
            intent.getSerializableExtra("retrievedBadge") as List<RetrievedBadge>? ?: emptyList()
        if (retrievedBadge.isNotEmpty()) {
            val badgeList = retrievedBadge.asReversed()
            badgeList.map { badge ->
                BadgeDialogFragment
                    .newInstance(badge.name, badge.imageUrl, homeViewModel.isFirstBadge)
                    .show(supportFragmentManager, "badgeDialog")
                with(homeViewModel) {
                    if (isFirstBadge) isFirstBadge = false
                }
            }
        }
    }

    private fun setInitListener() {
        binding.weeklyCalendar.setOnWeeklyDayClickListener { view, date ->
            binding.tvTargetMonth.text =
                date.format(DateTimeFormatter.ofPattern(TARGET_MONTH_PATTERN))

            lifecycleScope.launch {
                homeViewModel.getDateDiary(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))

                binding.btnWriteDiary.visibility =
                    if (LocalDate.now()
                            .isEqual(date) && homeViewModel.responseDateDiary.value == null
                    ) {
                        View.VISIBLE
                    } else {
                        View.INVISIBLE
                    }
            }
        }

        binding.weeklyCalendar.setOnWeeklyCalendarSwipeListener(object :
            OnWeeklyCalendarSwipeListener {
            override fun onSwipe(mondayDate: LocalDate?) {
                mondayDate?.let {
                    lifecycleScope.launch {
                        homeViewModel.getDiaries(
                            start = binding.weeklyCalendar.mondayDate?.plusDays(-6)?.format(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                            ) ?: "",
                            end = binding.weeklyCalendar.mondayDate?.plusDays(6)?.format(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                            ) ?: "",
                        )
                    }
                    setTargetMonthTitle()
                }
            }
        })
        binding.clDiaryList.setOnSingleClickListener {
            Intent(this, DiaryDetailActivity::class.java).apply {
                putExtra("diaryId", homeViewModel.responseDateDiary.value?.id)
            }.run(::startActivity)
        }
    }

    private fun setTargetMonthTitle() {
        binding.tvTargetMonth.text = binding.weeklyCalendar.mondayDate?.format(
            DateTimeFormatter.ofPattern(TARGET_MONTH_PATTERN),
        )
    }

    private fun observeData() {
        homeViewModel.responseDiaries.observe(this) { diarySummaries ->
            diarySummaries?.diaries?.let {
                val diaryEntry = it.map { diary ->
                    diary.value
                }
                binding.weeklyCalendar.setDiaryEntry(diaryEntry)
            }
        }

        homeViewModel.responseDateDiary.observe(this) {
            if (it == null) {
                binding.clDiaryList.visibility = View.GONE
                binding.clNoDiary.visibility = View.VISIBLE
            } else {
                binding.clDiaryList.visibility = View.VISIBLE
                binding.clNoDiary.visibility = View.GONE

                val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val outputFormatter = DateTimeFormatter.ofPattern("h : mm a", Locale.ENGLISH)

                val createdAtDateTime = LocalDateTime.parse(it.createdAt, inputFormatter)
                val formattedCreatedAt = createdAtDateTime.format(outputFormatter)

                binding.tvDiaryWritenTime.text = formattedCreatedAt
                binding.tvDiary.text = it.content
            }
        }
    }

    private fun getWeeklyDiary() {
        lifecycleScope.launch {
            homeViewModel.getDiaries(
                start = binding.weeklyCalendar.mondayDate?.plusDays(-6)?.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                ) ?: "",
                end = binding.weeklyCalendar.mondayDate?.plusDays(6)?.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                ) ?: "",
            )
        }
    }

    companion object {
        const val TARGET_MONTH_PATTERN = "yyyy년 M월"
    }
}
