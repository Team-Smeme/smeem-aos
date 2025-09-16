package com.sopt.smeem.presentation.bookmark

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sopt.smeem.R
import com.sopt.smeem.presentation.bookmark.contract.BookmarkIntent
import com.sopt.smeem.presentation.bookmark.contract.BookmarkItem
import com.sopt.smeem.presentation.bookmark.contract.BookmarkSideEffect
import com.sopt.smeem.presentation.bookmark.contract.BookmarkType
import com.sopt.smeem.presentation.compose.components.LoadingScreen
import com.sopt.smeem.presentation.compose.components.SmeemSkeleton
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.background
import com.sopt.smeem.presentation.compose.theme.gray250
import com.sopt.smeem.presentation.compose.theme.gray350
import com.sopt.smeem.presentation.compose.theme.gray900
import com.sopt.smeem.util.toTextDp
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    navBackStackEntry: NavBackStackEntry? = null,
    onNavigateToDetail: (Int) -> Unit = {}
) {
    val state by viewModel.collectAsState()

    // 북마크 상세에서 돌아올 때마다 목록 새로고침
    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.let {
            viewModel.onIntent(BookmarkIntent.LoadBookmarks)
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is BookmarkSideEffect.NavigateToDetail -> {
                onNavigateToDetail(sideEffect.bookmarkId)
            }

            is BookmarkSideEffect.ShowError -> {
                // TODO: 에러 처리
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Text(
            text = "북마크",
            style = Typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = gray900
            ),
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )

        when {
            state.isLoading -> {
                LoadingScreen()
            }

            state.bookmarks.isEmpty() -> {
                EmptyBookmarkContent()
            }

            else -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 12.dp,
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    items(state.bookmarks) { bookmark ->
                        BookmarkItem(
                            bookmark = bookmark,
                            onClick = { viewModel.onIntent(BookmarkIntent.OnBookmarkClick(bookmark.bookmarkId)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookmarkItem(
    bookmark: BookmarkItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .clickable { onClick() },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(
                    when (bookmark.type) {
                        BookmarkType.REELS -> 6f / 9f
                        BookmarkType.P -> 1f
                    }
                )
                .background(shape = RoundedCornerShape(8.dp), color = Color.Transparent)
                .clip(RoundedCornerShape(8.dp))
        ) {
            var isLoading by remember { mutableStateOf(!bookmark.thumbnailImageUrl.isBlank()) }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        bookmark.thumbnailImageUrl.ifBlank {
                            R.drawable.img_bookmark_error
                        }
                    )
                    .crossfade(true)
                    .listener(
                        onStart = { isLoading = !bookmark.thumbnailImageUrl.isBlank() },
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
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = bookmark.expression,
                style = Typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.dp.toTextDp(),
                    color = gray900
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = bookmark.description,
                style = Typography.labelSmall.copy(
                    color = gray900
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun EmptyBookmarkContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_warning),
                contentDescription = "warning",
                tint = gray250,
            )
            Spacer(modifier = Modifier.height(11.dp))


            Text(
                text = "아직 저장한 북마크가 없어요.",
                style = Typography.bodyMedium.copy(
                    fontSize = 15.dp.toTextDp(),
                    fontWeight = FontWeight.Medium,
                    color = gray350,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "인스타그램에서 공유 버튼을 눌러\n북마크를 저장해보세요!",
                style = Typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = gray250,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 인스타그램 열기 버튼
            InstagramOpenButton()
        }
    }
}

@Composable
private fun InstagramOpenButton() {
    val context = LocalContext.current

    Button(
        onClick = {
            try {
                val intent =
                    context.packageManager.getLaunchIntentForPackage("com.instagram.android")
                if (intent != null) {
                    context.startActivity(intent)
                } else {
                    val playStoreIntent = Intent(
                        Intent.ACTION_VIEW,
                        "market://details?id=com.instagram.android".toUri()
                    )
                    context.startActivity(playStoreIntent)
                }
            } catch (e: Exception) {
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=com.instagram.android".toUri()
                )
                context.startActivity(webIntent)
            }
        },
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(
            width = 1.dp,
            color = gray250
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = "인스타그램 열기",
            style = Typography.bodyMedium.copy(
                color = gray350,
                fontWeight = FontWeight.Medium,
                fontSize = 13.dp.toTextDp()
            )
        )
    }
}

@Preview
@Composable
private fun BookmarkItemPreview() {
    BookmarkItem(
        bookmark = BookmarkItem(
            bookmarkId = 1,
            thumbnailImageUrl = "https://picsum.photos/200/300",
            expression = "행복한 표정",
            type = BookmarkType.REELS,
            description = "",
            createdAt = ""
        ),
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyBookmarkStatePreview() {
    EmptyBookmarkContent()
}
