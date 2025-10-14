package com.sopt.smeem.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.repository.BookmarkRepository
import com.sopt.smeem.domain.repository.LocalRepository
import com.sopt.smeem.presentation.bookmark.contract.BookmarkIntent
import com.sopt.smeem.presentation.bookmark.contract.BookmarkItem
import com.sopt.smeem.presentation.bookmark.contract.BookmarkSideEffect
import com.sopt.smeem.presentation.bookmark.contract.BookmarkState
import com.sopt.smeem.presentation.bookmark.contract.BookmarkType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val localRepository: LocalRepository
) : ContainerHost<BookmarkState, BookmarkSideEffect>, ViewModel() {

    override val container: Container<BookmarkState, BookmarkSideEffect> = container(BookmarkState())

    private var isNavigating = false
    private var onNavigateToDetailCallback: ((Int) -> Unit)? = null

    fun setOnNavigateToDetailCallback(callback: (Int) -> Unit) {
        onNavigateToDetailCallback = callback
    }

    init {
        checkTutorialStatus()
        loadBookmarks()
    }

    fun onIntent(intent: BookmarkIntent) {
        when (intent) {
            is BookmarkIntent.LoadBookmarks -> {
                loadBookmarks()
            }
            is BookmarkIntent.OnBookmarkClick -> {
                if (isNavigating) return

                isNavigating = true
                onNavigateToDetailCallback?.invoke(intent.bookmarkId)

                // Reset navigation flag after delay
                viewModelScope.launch {
                    delay(500)
                    isNavigating = false
                }
            }
            is BookmarkIntent.CompleteTutorial -> {
                completeTutorial()
            }
            is BookmarkIntent.CheckTutorialStatus -> {
                checkTutorialStatus()
            }
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
            }.sortedByDescending { it.createdAt } // 최신순 정렬
            
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


    fun resetNavigatingFlag() {
        isNavigating = false
    }

    private fun checkTutorialStatus() = intent {
        try {
            val isCompleted = localRepository.isBookmarkTutorialCompleted()
            reduce { state.copy(shouldShowTutorial = !isCompleted) }
        } catch (e: Exception) {
            // 오류 발생시 튜토리얼을 표시하지 않음
            reduce { state.copy(shouldShowTutorial = false) }
        }
    }

    private fun completeTutorial() = intent {
        try {
            localRepository.setBookmarkTutorialCompleted()
            reduce { state.copy(shouldShowTutorial = false) }
        } catch (e: Exception) {
            postSideEffect(BookmarkSideEffect.ShowError("튜토리얼 상태 저장에 실패했습니다."))
        }
    }
}
