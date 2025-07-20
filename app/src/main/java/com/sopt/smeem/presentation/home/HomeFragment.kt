package com.sopt.smeem.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sopt.smeem.databinding.FragmentHomeBinding
import com.sopt.smeem.event.AmplitudeEventType
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.presentation.IntentConstants.DIARY_ID
import com.sopt.smeem.presentation.compose.components.Banner
import com.sopt.smeem.presentation.compose.theme.SmeemTheme
import com.sopt.smeem.presentation.detail.DiaryDetailActivity
import com.sopt.smeem.presentation.home.calendar.SmeemCalendar
import com.sopt.smeem.presentation.home.calendar.core.CalendarState
import com.sopt.smeem.presentation.home.calendar.core.Period
import com.sopt.smeem.presentation.mypage.MyPageActivity
import com.sopt.smeem.presentation.write.foreign.ForeignWriteActivity
import com.sopt.smeem.presentation.write.natiive.NativeWriteStep1Activity
import com.sopt.smeem.util.getWeekStartDate
import com.sopt.smeem.util.setComposeContent
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding)

    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val eventVm: EventVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = binding.composeCalendar
        val banner = binding.composeBanner
        val fab = binding.composeFabMenu

        initView(LocalDate.now())
        setInitListener()

        setComposeContent(calendar) {
            SmeemTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    SmeemCalendar(homeViewModel)
                }
            }
        }

        setComposeContent(fab) {
            SmeemTheme {
//                SmeemFabMenu()
            }
        }

//        observeBannerState(banner)
        moveToMyPage()
        observeData()
        eventVm.sendEvent(AmplitudeEventType.HOME_VIEW)

        homeViewModel.activeVisit { Timber.e("visit count 반영 실패.") }
    }

    override fun onResume() {
        super.onResume()
        initView(LocalDate.now())
    }

    private fun moveToMyPage() {
        binding.ivMyPage.setOnClickListener {
            startActivity(Intent(requireActivity(), MyPageActivity::class.java))
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
            }
        }
    }

    private fun setInitListener() {
        binding.clDiaryList.setOnSingleClickListener {
            Intent(requireActivity(), DiaryDetailActivity::class.java)
                .apply {
                    putExtra(DIARY_ID, homeViewModel.diaryList.value?.id)
                }.run(::startActivity)
        }
    }

    private fun observeData() {
        // 홈에 일기 띄우는 로직
        homeViewModel.diaryList.observe(viewLifecycleOwner) {
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

    private fun observeBannerState(bannerView: ComposeView) {
        lifecycleScope.launch {
            homeViewModel.configInfo
                .combine(homeViewModel.isBannerVisible) { configInfo, isVisible ->
                    Pair(configInfo, isVisible)
                }.collect { (configInfo, isVisible) ->
                    setComposeContent(bannerView) {
                        SmeemTheme {
                            if (isVisible) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.background,
                                ) {
                                    Banner(
                                        title = configInfo.bannerTitle,
                                        content = configInfo.bannerContent,
                                        onBannerClick = {
                                            handleBannerClickEvent(configInfo)

                                            eventVm.sendEvent(
                                                AmplitudeEventType.BANNER_CLICK,
                                                mapOf(AmplitudeEventType.BANNER_CLICK.propertyKey.toString() to true)
                                            )
                                        },
                                        onBannerClose = {
                                            homeViewModel.closeBanner()

                                            eventVm.sendEvent(
                                                AmplitudeEventType.BANNER_X,
                                                mapOf(AmplitudeEventType.BANNER_CLICK.propertyKey.toString() to true)
                                            )
                                        },
                                        modifier =
                                        Modifier.padding(
                                            horizontal = 18.dp,
                                            vertical = 12.dp,
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }

    /**
     * 배너 클릭 이벤트 처리
     * 외부 이벤트일 시 크롬 탭으로 URL 열기
     * 내부 이벤트일 시 내부 화면으로 이동
     */
    private fun handleBannerClickEvent(configInfo: ConfigInfo) {
        if (configInfo.isExternalEvent) {
            CustomTabsIntent.Builder().build().run {
                launchUrl(requireActivity(), configInfo.bannerEventPath.toUri())
            }
        } else {
            when (configInfo.bannerEventPath) {
                // 한국어 일기 작성
                "native_write_diary" -> {
                    startActivity(Intent(requireActivity(), NativeWriteStep1Activity::class.java))
                }
                // 영어 일기 작성
                "foreign_write_diary" -> {
                    startActivity(Intent(requireActivity(), ForeignWriteActivity::class.java))
                }
                // 마이페이지 - 성과 요약
                "my_page" -> {
                    startActivity(Intent(requireActivity(), MyPageActivity::class.java))
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

//@OptIn(ExperimentalMaterial3ExpressiveApi::class)
//@Composable
//fun SmeemFabMenu() {
//    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
//    val context = LocalContext.current
//    val rotationAngle by animateFloatAsState(
//        targetValue = if (fabMenuExpanded) 45f else 0f,
//        label = "fab rotation"
//    )
//
//    FloatingActionButtonMenu(
//        expanded = fabMenuExpanded,
//        button = {
//            ToggleFloatingActionButton(
//                checked = fabMenuExpanded,
//                onCheckedChange = { fabMenuExpanded = it },
//                containerColor = SmeemTheme.colors.point,
//                contentColor = SmeemTheme.colors.white
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "fab",
//                    modifier = Modifier.rotate(rotationAngle)
//                )
//            }
//        }
//    ) {
//        FloatingActionButtonMenuItem(
//            onClick = {
//                context.startActivityOf<NativeWriteStep1Activity>()
//                fabMenuExpanded = false
//            },
//            text = { Text(text = context.getString(R.string.fab_random_subject)) },
//        )
//        FloatingActionButtonMenuItem(
//            onClick = {
//                context.startActivityOf<ForeignWriteActivity>()
//                fabMenuExpanded = false
//            },
//            text = { Text(text = context.getString(R.string.fab_write_diary)) },
//        )
//    }
//}

//@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Composable
//fun HomeFabMenuPreview() {
//    SmeemTheme {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            SmeemFabMenu()
//        }
//    }
//}

