package com.sopt.smeem.presentation.bookmark

import androidx.lifecycle.ViewModel
import com.sopt.smeem.domain.repository.BookmarkRepository
import com.sopt.smeem.presentation.bookmark.contract.BookmarkIntent
import com.sopt.smeem.presentation.bookmark.contract.BookmarkItem
import com.sopt.smeem.presentation.bookmark.contract.BookmarkSideEffect
import com.sopt.smeem.presentation.bookmark.contract.BookmarkState
import com.sopt.smeem.presentation.bookmark.contract.BookmarkType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) : ContainerHost<BookmarkState, BookmarkSideEffect>, ViewModel() {
    
    override val container: Container<BookmarkState, BookmarkSideEffect> = container(BookmarkState())

    init {
        loadBookmarks()
    }

    fun onIntent(intent: BookmarkIntent) {
        when (intent) {
            is BookmarkIntent.LoadBookmarks -> loadBookmarks()
            is BookmarkIntent.OnBookmarkClick -> onBookmarkClick(intent.bookmarkId)
        }
    }

    private fun loadBookmarks() = intent {
        reduce { state.copy(isLoading = true, error = null) }
        
        try {
            val response = bookmarkRepository.getBookmarks()
            val bookmarks = response.data().bookmarks.mapNotNull { dto ->
                val bookmarkId = dto.bookmarkId ?: return@mapNotNull null
                
                BookmarkItem(
                    bookmarkId = bookmarkId,
                    thumbnailImageUrl = dto.thumbnailImageUrl ?: "",
                    expression = dto.expression ?: "표현식 없음",
                    type = BookmarkType.fromString(dto.scrapType),
                    description = dto.description ?: "",
                    createdAt = dto.createdAt ?: ""
                )
            }
            
            reduce { 
                state.copy(
                    isLoading = false, 
                    bookmarks = bookmarks,
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
            postSideEffect(BookmarkSideEffect.ShowError(e.message ?: "알 수 없는 오류가 발생했습니다."))
        }
    }

    private fun onBookmarkClick(bookmarkId: Int) = intent {
        postSideEffect(BookmarkSideEffect.NavigateToDetail(bookmarkId))
    }
}
