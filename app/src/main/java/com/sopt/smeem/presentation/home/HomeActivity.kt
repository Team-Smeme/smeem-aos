package com.sopt.smeem.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import com.sopt.smeem.DefaultSnackBar
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityHomeBinding
import com.sopt.smeem.domain.model.RetrievedBadge
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.detail.DiaryDetailActivity
import com.sopt.smeem.presentation.home.WritingBottomSheet.Companion.TAG
import com.sopt.smeem.presentation.home.calendar.SmeemCalendarImpl
import com.sopt.smeem.presentation.home.calendar.core.CalendarIntent
import com.sopt.smeem.presentation.home.calendar.core.Period
import com.sopt.smeem.presentation.home.calendar.ui.theme.SmeemTheme
import com.sopt.smeem.presentation.mypage.MyPageActivity
import com.sopt.smeem.util.getWeekStartDate
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class HomeActivity : BindingActivity<ActivityHomeBinding>(R.layout.activity_home) {

    lateinit var bs: WritingBottomSheet

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calendar = binding.composeCalendar
        bs = WritingBottomSheet()
        initView(LocalDate.now())
        setInitListener()
        calendar.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SmeemTheme {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        SmeemCalendar()
                    }
                }
            }
        }

        moveToMyPage()
        observeData()
        onTouchWrite()
    }

    override fun onResume() {
        super.onResume()
        initView(LocalDate.now())
    }

    override fun onRestart() {
        super.onRestart()
        initView(LocalDate.now())
    }

    @Composable
    fun SmeemCalendar() {
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        val scrollState = rememberScrollState()

        Column(Modifier.verticalScroll(scrollState)) {
            SmeemCalendarImpl(
                onDayClick = { selectedDate = it }
            )
        }
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

    private fun initView(day: LocalDate) {
        lifecycleScope.launch {
            with(homeViewModel) {
                onIntent(
                    CalendarIntent.LoadNextDates(
                        startDate = day.minusWeeks(1).getWeekStartDate(),
                        period = Period.WEEK
                    )
                )
                onIntent(CalendarIntent.SelectDate(date = day))
            }
            binding.btnWriteDiary.visibility =
                if (homeViewModel.diaryList.value == null) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
        }
        showDiaryCompleted()
        showBadgeDialog()
    }

    private fun showDiaryCompleted() {
        val msg = intent.getStringExtra("snackbarText")
        if (msg != null) {
            DefaultSnackBar.make(binding.root, msg).show()
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
        binding.clDiaryList.setOnSingleClickListener {
            Intent(this, DiaryDetailActivity::class.java).apply {
                putExtra("diaryId", homeViewModel.diaryList.value?.id)
            }.run(::startActivity)
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            homeViewModel.selectedDate.collect {
                when {
                    homeViewModel.diaryList.value != null -> binding.btnWriteDiary.visibility =
                        View.INVISIBLE

                    it == LocalDate.now() -> binding.btnWriteDiary.visibility = View.VISIBLE
                    else -> binding.btnWriteDiary.visibility = View.INVISIBLE
                }
            }
        }
        // 홈에 일기 띄우는 로직
        homeViewModel.diaryList.observe(this) {
            val timeFormatter = DateTimeFormatter.ofPattern("h : mm a", Locale.ENGLISH)

            with(homeViewModel.diaryList.value) {
                if (this == null) {
                    binding.clDiaryList.visibility = View.GONE
                    binding.clNoDiary.visibility = View.VISIBLE
                } else {
                    binding.clDiaryList.visibility = View.VISIBLE
                    binding.clNoDiary.visibility = View.GONE
                    binding.tvDiaryWritenTime.text = this.createdAt.format(timeFormatter)
                    binding.tvDiary.text = this.content
                }
            }
        }
    }

    companion object {
        const val TARGET_MONTH_PATTERN = "yyyy년 M월"
    }
}
