package com.sopt.smeem.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import com.sopt.smeem.DefaultSnackBar
import com.sopt.smeem.R
import com.sopt.smeem.data.SmeemDataStore.RECENT_DIARY_DATE
import com.sopt.smeem.data.SmeemDataStore.dataStore
import com.sopt.smeem.databinding.ActivityHomeBinding
import com.sopt.smeem.domain.dto.RetrievedBadgeDto
import com.sopt.smeem.event.AmplitudeEventType
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.presentation.IntentConstants.DIARY_ID
import com.sopt.smeem.presentation.IntentConstants.RETRIEVED_BADGE_DTO
import com.sopt.smeem.presentation.IntentConstants.SNACKBAR_TEXT
import com.sopt.smeem.presentation.compose.theme.SmeemTheme
import com.sopt.smeem.presentation.detail.DiaryDetailActivity
import com.sopt.smeem.presentation.home.WritingBottomSheet.Companion.TAG
import com.sopt.smeem.presentation.home.calendar.SmeemCalendar
import com.sopt.smeem.presentation.home.calendar.core.CalendarState
import com.sopt.smeem.presentation.home.calendar.core.Period
import com.sopt.smeem.presentation.mypage.TempMyPageActivity
import com.sopt.smeem.util.DateUtil
import com.sopt.smeem.util.getWeekStartDate
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class HomeActivity : BindingActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private lateinit var bs: WritingBottomSheet

    private val homeViewModel by viewModels<HomeViewModel>()
    private val eventVm: EventVM by viewModels()

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
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        SmeemCalendar(homeViewModel)
                    }
                }
            }
        }

        moveToMyPage()
        observeData()
        onTouchWrite()
        eventVm.sendEvent(AmplitudeEventType.HOME_VIEW)
        homeViewModel.activeVisit { Timber.e("visit count 반영 실패. ", it)}
    }

    override fun onResume() {
        super.onResume()
        initView(LocalDate.now())
    }

    override fun onRestart() {
        super.onRestart()
        initView(LocalDate.now())
    }

    private fun onTouchWrite() {
        binding.btnWriteDiary.setOnSingleClickListener {
            bs.show(supportFragmentManager, TAG)
        }
    }

    private fun moveToMyPage() {
        binding.ivMyPage.setOnClickListener {
            startActivity(Intent(this, TempMyPageActivity::class.java))
        }
    }

    private fun initView(day: LocalDate) {
        lifecycleScope.launch {
            with(homeViewModel) {
                onStateChange(
                    CalendarState.LoadNextDates(
                        startDate = day.minusWeeks(1).getWeekStartDate(),
                        period = Period.WEEK,
                    ),
                )
                onStateChange(CalendarState.SelectDate(date = day))
                updateWriteDiaryButtonVisibility()
            }
        }
        showDiaryCompleted()
        showBadgeDialog()
    }

    private fun showDiaryCompleted() {
        val msg = intent.getStringExtra(SNACKBAR_TEXT)
        if (msg != null) {
            DefaultSnackBar.make(binding.root, msg).show()
            intent.removeExtra(SNACKBAR_TEXT)
        }
    }

    private fun showBadgeDialog() {
        val retrievedBadge =
            intent.getSerializableExtra(RETRIEVED_BADGE_DTO) as List<RetrievedBadgeDto>?
                ?: emptyList()
        if (retrievedBadge.isNotEmpty()) {
            val badgeList = retrievedBadge.asReversed()
            badgeList.map { badge ->
                supportFragmentManager
                    .beginTransaction()
                    .add(
                        BadgeDialogFragment.newInstance(
                            badge.name,
                            badge.imageUrl,
                            homeViewModel.isFirstBadge
                        ), "badgeDialog"
                    )
                    .commitAllowingStateLoss()
                with(homeViewModel) {
                    if (isFirstBadge) isFirstBadge = false
                }
            }
            intent.removeExtra(RETRIEVED_BADGE_DTO)
        }
    }

    private suspend fun updateWriteDiaryButtonVisibility() {
        val recentDiaryDateFlow: Flow<String> = dataStore.data
            .map { storage ->
                storage[RECENT_DIARY_DATE] ?: "2023-01-14"
            }

        recentDiaryDateFlow.collect { date ->
            val recent = DateUtil.asLocalDate(date)
            val isTodaySelected = homeViewModel.selectedDate.value == LocalDate.now()
            val isTodayDiaryWritten = recent == LocalDate.now()

            binding.btnWriteDiary.visibility = if (isTodaySelected && !isTodayDiaryWritten) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }

    private fun setInitListener() {
        binding.clDiaryList.setOnSingleClickListener {
            Intent(this, DiaryDetailActivity::class.java).apply {
                putExtra(DIARY_ID, homeViewModel.diaryList.value?.id)
            }.run(::startActivity)
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            homeViewModel.selectedDate.collectLatest {
                updateWriteDiaryButtonVisibility()
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
}
