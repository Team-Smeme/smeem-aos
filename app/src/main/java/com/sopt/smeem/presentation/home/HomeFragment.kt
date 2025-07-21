package com.sopt.smeem.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
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
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.gray300
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.white
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
import com.sopt.smeem.util.toTextDp
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
                SmeemFabMenu()
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SmeemFabMenu(modifier: Modifier = Modifier) {
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    
    val rotationAngle by animateFloatAsState(
        targetValue = if (fabMenuExpanded) 45f else 0f,
        label = "fab rotation"
    )
    
    val onMenuClose = remember { { fabMenuExpanded = false } }

    Box(modifier = modifier) {
        FloatingActionButtonMenu(
            expanded = fabMenuExpanded,
            modifier = Modifier
                .graphicsLayer { shadowElevation = 0f }
                .shadow(0.dp),
            button = {
                ToggleFloatingActionButton(
                    checked = fabMenuExpanded,
                    onCheckedChange = { fabMenuExpanded = it },
                    containerColor = { point },
                    modifier = Modifier
                        .graphicsLayer { shadowElevation = 0f }
                        .clip(CircleShape),
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "fab",
                            modifier = Modifier.rotate(rotationAngle),
                            tint = white
                        )
                    }
                )
            }
        ) {
            if (fabMenuExpanded) {
                MenuContent(
                    onForeignWriteClick = {
                        context.startActivity(Intent(context, ForeignWriteActivity::class.java))
                        onMenuClose()
                    },
                    onNativeWriteClick = {
                        context.startActivity(Intent(context, NativeWriteStep1Activity::class.java))
                        onMenuClose()
                    }
                )
            }
        }
    }
}

@Composable
private fun MenuContent(
    onForeignWriteClick: () -> Unit,
    onNativeWriteClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .width(190.dp)
                .wrapContentHeight()
                .border(
                    width = 1.dp,
                    color = gray300,
                    shape = RoundedCornerShape(6.dp)
                )
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
        ) {
            Column {
                MenuItemBox(
                    onClick = onForeignWriteClick,
                    highlightText = "외국어로 바로 ",
                    normalText = "작성하기"
                )

                HorizontalDivider(
                    thickness = 2.dp,
                    color = gray100,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                MenuItemBox(
                    onClick = onNativeWriteClick,
                    highlightText = "한국어로 먼저 ",
                    normalText = "작성하기"
                )
            }
        }
    }
}

@Composable
private fun MenuItemBox(
    onClick: () -> Unit,
    highlightText: String,
    normalText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = createStyledText(highlightText, normalText),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun createStyledText(highlightText: String, normalText: String) = buildAnnotatedString {
    withStyle(
        style = SpanStyle(
            color = point,
            fontSize = 16.dp.toTextDp(),
            fontWeight = Typography.titleMedium.fontWeight,
            fontFamily = Typography.titleMedium.fontFamily
        )
    ) {
        append(highlightText)
    }
    withStyle(
        style = SpanStyle(
            color = black,
            fontSize = 16.dp.toTextDp(),
            fontWeight = Typography.titleSmall.fontWeight,
            fontFamily = Typography.titleSmall.fontFamily
        )
    ) {
        append(normalText)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun HomeFabMenuPreview() {
    SmeemTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SmeemFabMenu()
        }
    }
}

