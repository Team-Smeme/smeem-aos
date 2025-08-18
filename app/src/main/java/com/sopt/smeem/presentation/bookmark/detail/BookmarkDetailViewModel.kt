package com.sopt.smeem.presentation.bookmark.detail

import androidx.lifecycle.ViewModel
import com.sopt.smeem.presentation.bookmark.detail.contract.BookmarkDetailIntent
import com.sopt.smeem.presentation.bookmark.detail.contract.BookmarkDetailItem
import com.sopt.smeem.presentation.bookmark.detail.contract.BookmarkDetailSideEffect
import com.sopt.smeem.presentation.bookmark.detail.contract.BookmarkDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BookmarkDetailViewModel @Inject constructor() :
    ContainerHost<BookmarkDetailState, BookmarkDetailSideEffect>, ViewModel() {

    override val container: Container<BookmarkDetailState, BookmarkDetailSideEffect> =
        container(BookmarkDetailState())

    fun onIntent(intent: BookmarkDetailIntent) {
        when (intent) {
            is BookmarkDetailIntent.LoadBookmarkDetail -> loadBookmarkDetail(intent.bookmarkId)
            is BookmarkDetailIntent.OnBackClick -> onBackClick()
            is BookmarkDetailIntent.OnMoreClick -> onMoreClick()
            is BookmarkDetailIntent.OnUseExpressionClick -> onUseExpressionClick()
            is BookmarkDetailIntent.OnInstagramClick -> onInstagramClick()
        }
    }

    private fun loadBookmarkDetail(bookmarkId: Int) = intent {
        reduce { state.copy(isLoading = true, error = null) }

        try {
            val bookmarkDetail = BookmarkDetailItem(
                thumbnailImageUrl = "https://shorturl.at/0ToBd",
                scrapedUrl = "https://shorturl.at/YwTvv",
                expression = "can you make up our room?",
                translatedExpression = "방 청소해주세요",
                description = "오늘의 문장 ✅ Can you make up clean our room please?",
                scrapType = "REELS"
            )

            reduce {
                state.copy(
                    isLoading = false,
                    bookmarkDetail = bookmarkDetail,
                    error = null
                )
            }
        } catch (e: Exception) {
            reduce {
                state.copy(
                    isLoading = false,
                    error = e.message ?: "알 수 없는 오류가 발생했습니다."
                )
            }
            postSideEffect(BookmarkDetailSideEffect.ShowError(e.message ?: "알 수 없는 오류가 발생했습니다."))
        }
    }

    private fun onBackClick() = intent {
        postSideEffect(BookmarkDetailSideEffect.NavigateBack)
    }

    private fun onMoreClick() = intent {
        postSideEffect(BookmarkDetailSideEffect.ShowMoreOptions)
    }

    private fun onUseExpressionClick() = intent {
        postSideEffect(BookmarkDetailSideEffect.NavigateToUseExpression)
    }

    private fun onInstagramClick() = intent {
        state.bookmarkDetail?.let { detail ->
            postSideEffect(BookmarkDetailSideEffect.OpenInstagram(detail.scrapedUrl))
        }
    }
}
