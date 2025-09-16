package com.sopt.smeem.presentation.bookmark

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.gray900
import com.sopt.smeem.util.toTextDp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkTutorialScreen(
    onTutorialCompleted: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { 4 })

    // 반응형 이미지 크기 계산
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = screenWidth > 600.dp
    val maxImageWidth = if (isTablet) 400.dp else screenWidth - 40.dp // 패딩 제외

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(96.dp))

        val currentStep = pagerState.currentPage + 1

        TutorialText(step = currentStep)

        Spacer(modifier = Modifier.height(if (currentStep == 4) 15.dp else 56.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(TUTORIAL_IMAGE_ASPECT_RATIO)
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = getTutorialImageResource(page + 1)),
                    contentDescription = "Tutorial Step ${page + 1}",
                    modifier = Modifier
                        .widthIn(max = maxImageWidth - 40.dp) // 패딩 제외
                        .aspectRatio(TUTORIAL_IMAGE_ASPECT_RATIO)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index)
                                gray900
                            else
                                gray100
                        )
                )
                if (index < 3) {
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (pagerState.currentPage == 3) {
            Button(
                onClick = onTutorialCompleted,
                colors = ButtonDefaults.buttonColors(
                    containerColor = gray900
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_instagram),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 8.dp)
                    )

                    Text(
                        text = "북마크로 영어 공부 시작하기",
                        color = Color.White,
                        fontSize = 15.dp.toTextDp(),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun TutorialText(step: Int) {
    Text(
        text = "Step $step",
        style = typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = gray900
        ),
        modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
    )

    val (title, subtitle) = when (step) {
        1 -> "인스타그램 릴스에서\n종이비행기 아이콘 클릭" to ""
        2 -> "하단 중앙에\n공유하기 클릭" to ""
        3 -> "앱 목록을 스와이프해\n더보기 클릭" to ""
        4 -> "목록에서 스밈 선택하면\n영어 표현이 자동 추출되어 북마크에 저장돼요!" to "아직 본문이 충분한 콘텐츠만 지원하고 있어요.\n저장시 유의해주세요!"
        else -> "" to ""
    }

    Text(
        text = title,
        style = typography.bodyLarge.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp,
            color = gray900,
            lineHeight = 1.4.em
        ),
        modifier = Modifier.padding(start = 20.dp, bottom = if (subtitle.isNotEmpty()) 8.dp else 0.dp)
    )

    if (subtitle.isNotEmpty()) {
        Text(
            text = subtitle,
            fontSize = 12.dp.toTextDp(),
            fontWeight = FontWeight.Normal,
            color = gray900.copy(alpha = 0.8f),
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}

private const val TUTORIAL_IMAGE_ASPECT_RATIO = 335f / 289f // 피그마 디자인 기준

private fun getTutorialImageResource(step: Int): Int {
    return when (step) {
        1 -> R.drawable.img_bookmark_tutorial_1
        2 -> R.drawable.img_bookmark_tutorial_2
        3 -> R.drawable.img_bookmark_tutorial_3
        4 -> R.drawable.img_bookmark_tutorial_4
        else -> R.drawable.img_bookmark_error // fallback
    }
}

@Preview(name = "Full Tutorial - Phone", showBackground = true)
@Composable
private fun BookmarkTutorialPhonePreview() {
    BookmarkTutorialScreen()
}

@Preview(name = "Full Tutorial - Tablet", device = Devices.TABLET, showBackground = true)
@Composable
private fun BookmarkTutorialTabletPreview() {
    BookmarkTutorialScreen()
}

@Preview(
    name = "Full Tutorial - Foldable",
    device = "spec:width=673dp,height=841dp",
    showBackground = true
)
@Composable
private fun BookmarkTutorialFoldablePreview() {
    BookmarkTutorialScreen()
}
