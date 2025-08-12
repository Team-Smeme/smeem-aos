package com.sopt.smeem.presentation.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sopt.smeem.presentation.bookmark.contract.BookmarkIntent
import com.sopt.smeem.presentation.bookmark.contract.BookmarkItem
import com.sopt.smeem.presentation.bookmark.contract.BookmarkSideEffect
import com.sopt.smeem.presentation.bookmark.contract.BookmarkType
import com.sopt.smeem.presentation.compose.components.LoadingScreen
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.background
import com.sopt.smeem.presentation.compose.theme.gray900
import com.sopt.smeem.util.toTextDp
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit = {}
) {
    val state by viewModel.collectAsState()

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
        TopAppBar(
            title = {
                Text(
                    text = "북마크",
                    style = Typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = gray900
                    )
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = background
            )
        )

        when {
            state.isLoading -> {
                LoadingScreen()
            }

            else -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 12.dp,
                    contentPadding = PaddingValues(vertical = 18.dp)
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(bookmark.thumbnailImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(
                    when (bookmark.type) {
                        BookmarkType.REELS -> 6f / 9f
                        BookmarkType.P -> 1f
                    }
                )
                .background(shape = RoundedCornerShape(8.dp), color = Color.Transparent)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

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
