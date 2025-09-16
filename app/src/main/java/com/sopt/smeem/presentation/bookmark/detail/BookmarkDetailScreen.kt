package com.sopt.smeem.presentation.bookmark.detail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sopt.smeem.R
import com.sopt.smeem.data.SmeemDataStore.RECENT_DIARY_DATE
import com.sopt.smeem.data.SmeemDataStore.dataStore
import com.sopt.smeem.presentation.bookmark.contract.BookmarkType
import com.sopt.smeem.presentation.bookmark.detail.contract.BookmarkDetailIntent
import com.sopt.smeem.presentation.bookmark.detail.contract.BookmarkDetailItem
import com.sopt.smeem.presentation.bookmark.detail.contract.BookmarkDetailSideEffect
import com.sopt.smeem.presentation.compose.components.SmeemSkeleton
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.background
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray200
import com.sopt.smeem.presentation.compose.theme.gray25
import com.sopt.smeem.presentation.compose.theme.gray900
import com.sopt.smeem.presentation.compose.theme.white
import com.sopt.smeem.util.DateUtil
import com.sopt.smeem.util.VerticalSpacer
import com.sopt.smeem.util.toTextDp
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate

@Composable
fun BookmarkDetailScreen(
    bookmarkId: Int? = null,
    url: String? = null,
    viewModel: BookmarkDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToUseExpression: (String) -> Unit = {}
) {
    
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(bookmarkId, url) {
        when {
            bookmarkId != null -> {
                viewModel.onIntent(BookmarkDetailIntent.LoadBookmarkDetail(bookmarkId))
            }
            url != null -> {
                viewModel.onIntent(BookmarkDetailIntent.CreateBookmarkFromUrl(url))
            }
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is BookmarkDetailSideEffect.NavigateBack -> {
                onNavigateBack()
            }

            is BookmarkDetailSideEffect.ShowError -> {
                // TODO: 에러 처리
            }

            is BookmarkDetailSideEffect.ShowMoreOptions -> {
                // TODO: 더보기 옵션 처리
            }

            is BookmarkDetailSideEffect.NavigateToUseExpression -> {
                state.bookmarkDetail?.let { detail ->
                    onNavigateToUseExpression("${detail.expression} ${detail.translatedExpression}")
                }
            }

            is BookmarkDetailSideEffect.OpenInstagram -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, sideEffect.url.toUri())
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // TODO: 에러 처리
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        when {
                state.isLoading -> {
                    BookmarkAILoadingScreen()
                }

                state.bookmarkDetail != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        HeaderSection(
                            onBackClick = { viewModel.onIntent(BookmarkDetailIntent.OnBackClick) },
                            onMoreClick = { viewModel.onIntent(BookmarkDetailIntent.OnMoreClick) }
                        )
                        
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                ImageAndInstagramSection(
                                    bookmarkDetail = state.bookmarkDetail!!,
                                    onImageClick = { viewModel.onIntent(BookmarkDetailIntent.OnInstagramClick) },
                                    onInstagramClick = { viewModel.onIntent(BookmarkDetailIntent.OnInstagramClick) }
                                )
                            }
                            
                            stickyHeader {
                                StickyExpressionTexts(
                                    expression = state.bookmarkDetail!!.expression,
                                    translatedExpression = state.bookmarkDetail!!.translatedExpression
                                )
                            }
                            
                            item {
                                ContentSection(
                                    description = state.bookmarkDetail!!.description
                                )
                            }
                        }
                    }
                }

                state.error != null -> {
                    // TODO: error 화면 대응
                }
            }

        if (state.bookmarkDetail != null) {
            val recentDiaryDateFlow = context.dataStore.data.map { storage ->
                storage[RECENT_DIARY_DATE] ?: "2023-01-14"
            }
            val recentDiaryDate by recentDiaryDateFlow.collectAsState(initial = "2023-01-14")
            
            val isTodayDiaryWritten = try {
                DateUtil.asLocalDate(recentDiaryDate) == LocalDate.now()
            } catch (e: Exception) {
                false
            }
            
            val shouldShowFab = !isTodayDiaryWritten
            
            if (shouldShowFab) {
                FloatingActionButton(
                onClick = { viewModel.onIntent(BookmarkDetailIntent.OnUseExpressionClick) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 21.dp),
                containerColor = black,
                shape = RoundedCornerShape(50.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 20.dp,
                        top = 14.dp,
                        bottom = 14.dp
                    )
                ) {

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                        contentDescription = "plus",
                        tint = white
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "이 표현 써보기",
                        style = Typography.bodyMedium.copy(
                            color = white,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.dp.toTextDp()
                        )
                    )
                }
            }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(gray25)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clickable { onBackClick() }
                .padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_sm),
                contentDescription = "뒤로가기",
                tint = gray900
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "북마크",
                style = Typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.dp.toTextDp(),
                    color = gray900
                )
            )
        }

        IconButton(
            onClick = { onMoreClick() }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_menu_more),
                contentDescription = "더보기",
                tint = gray900
            )
        }
    }
}

@Composable
private fun ImageAndInstagramSection(
    bookmarkDetail: BookmarkDetailItem,
    onImageClick: () -> Unit,
    onInstagramClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().background(gray25),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        BookmarkImage(
            imageUrl = bookmarkDetail.thumbnailImageUrl,
            scrapType = bookmarkDetail.scrapType,
            onClick = onImageClick
        )

        Spacer(modifier = Modifier.height(10.dp))

        InstagramBox(onClick = onInstagramClick)

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun StickyExpressionTexts(
    expression: String,
    translatedExpression: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(gray25),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ExpressionTexts(
            expression = expression,
            translatedExpression = translatedExpression
        )

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(
            color = gray200,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ContentSection(
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        DescriptionContent(description = description)

        Spacer(modifier = Modifier.height(88.dp))
    }
}


@Composable
private fun InstagramBox(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, gray200, shape = RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_link),
            contentDescription = "Instagram",
            tint = gray900,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Instagram",
            style = Typography.labelSmall.copy(
                color = gray900,
            )
        )
    }
}

@Composable
private fun ExpressionTexts(
    expression: String,
    translatedExpression: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = expression,
            style = Typography.titleMedium.copy(
                color = gray900,
                lineHeight = 1.4.em,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                )
            ),
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = translatedExpression,
            style = Typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                color = gray900,
                fontSize = 15.dp.toTextDp(),
                lineHeight = 1.4.em,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                )
            ),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BookmarkImage(
    imageUrl: String,
    scrapType: String,
    onClick: () -> Unit
) {
    val aspectRatio = when (scrapType) {
        BookmarkType.REELS.name -> 9f / 6f
        else -> 1f
    }

    val height = (132.dp * aspectRatio)

    Box(
        modifier = Modifier
            .width(132.dp)
            .height(height)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        var isLoading by remember { mutableStateOf(imageUrl.isNotBlank()) }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    imageUrl.ifBlank {
                        R.drawable.img_bookmark_error
                    }
                )
                .crossfade(true)
                .listener(
                    onStart = { isLoading = imageUrl.isNotBlank() },
                    onSuccess = { _, _ -> isLoading = false },
                    onError = { _, _ -> isLoading = false }
                )
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isLoading) {
            SmeemSkeleton(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun extractMainContent(description: String): String {
    // 패턴: ": \"본문 내용\"." 부분에서 본문만 추출
    val startIndex = description.indexOf(": \"")
    val endIndex = description.lastIndexOf("\".")
    
    return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
        description.substring(startIndex + 3, endIndex).trim()
    } else {
        description // 패턴이 맞지 않으면 원본 반환
    }
}

@Composable
private fun DescriptionContent(description: String) {
    val extractedContent = extractMainContent(description)
    
    Text(
        text = extractedContent,
        style = Typography.bodyMedium.copy(
            color = gray900,
            fontSize = 15.dp.toTextDp(),
            fontWeight = FontWeight.Normal,
            lineHeight = 1.5.em
        )
    )
}

@Composable
fun BookmarkAILoadingScreen(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.smeem_loading))

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxWidth()
                .height(164.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )

        VerticalSpacer(8.dp)

        Text(
            text = stringResource(R.string.bookmark_ai_loading_description),
            style = Typography.bodySmall,
            color = black,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkAILoadingScreenPreview() {
    BookmarkAILoadingScreen()
}

@Preview(showBackground = true)
@Composable
private fun StickyExpressionTextsPreview() {
    StickyExpressionTexts(
        expression = "Pissed off",
        translatedExpression = "역정 했니다"
    )
}
