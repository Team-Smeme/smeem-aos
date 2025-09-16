package com.sopt.smeem.presentation.bookmark.detail

import androidx.lifecycle.ViewModel
import com.sopt.smeem.domain.repository.BookmarkRepository
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
class BookmarkDetailViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) : ContainerHost<BookmarkDetailState, BookmarkDetailSideEffect>, ViewModel() {

    override val container: Container<BookmarkDetailState, BookmarkDetailSideEffect> =
        container(BookmarkDetailState())

    fun onIntent(intent: BookmarkDetailIntent) {
        when (intent) {
            is BookmarkDetailIntent.LoadBookmarkDetail -> loadBookmarkDetail(intent.bookmarkId)
            is BookmarkDetailIntent.CreateBookmarkFromUrl -> createBookmarkFromUrl(intent.url)
            is BookmarkDetailIntent.OnBackClick -> onBackClick()
            is BookmarkDetailIntent.OnMoreClick -> onMoreClick()
            is BookmarkDetailIntent.OnUseExpressionClick -> onUseExpressionClick()
            is BookmarkDetailIntent.OnInstagramClick -> onInstagramClick()
        }
    }

    private fun loadBookmarkDetail(bookmarkId: Int) = intent {
        reduce { state.copy(isLoading = true, error = null) }

        try {
            val response = bookmarkRepository.getBookmarkDetail(bookmarkId)
            val data = response.data()
            
            val bookmarkDetail = BookmarkDetailItem(
                thumbnailImageUrl = data.thumbnailImageUrl,
                scrapedUrl = data.scrapedUrl,
                expression = data.expression,
                translatedExpression = data.translatedExpression,
                description = data.description,
                scrapType = data.scrapType
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
    
    private fun createBookmarkFromUrl(url: String) = intent {
        reduce { state.copy(isLoading = true, error = null) }

        try {
            val response = bookmarkRepository.createBookmark(url)
            val data = response.data()
            
            val bookmarkDetail = BookmarkDetailItem(
                thumbnailImageUrl = data.thumbnailImageUrl,
                scrapedUrl = data.scrapedUrl,
                expression = data.expression,
                translatedExpression = data.translatedExpression,
                description = data.description,
                scrapType = data.scrapType
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
}
