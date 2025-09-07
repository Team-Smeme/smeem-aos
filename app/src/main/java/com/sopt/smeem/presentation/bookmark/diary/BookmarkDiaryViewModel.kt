package com.sopt.smeem.presentation.bookmark.diary

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.sopt.smeem.domain.dto.WriteDiaryRequestDto
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.presentation.bookmark.diary.contract.BookmarkDiaryIntent
import com.sopt.smeem.presentation.bookmark.diary.contract.BookmarkDiarySideEffect
import com.sopt.smeem.presentation.bookmark.diary.contract.BookmarkDiaryState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BookmarkDiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ContainerHost<BookmarkDiaryState, BookmarkDiarySideEffect>, ViewModel() {

    override val container: Container<BookmarkDiaryState, BookmarkDiarySideEffect> =
        container(BookmarkDiaryState())

    fun onIntent(intent: BookmarkDiaryIntent) {
        when (intent) {
            is BookmarkDiaryIntent.OnDiaryTextChange -> onDiaryTextChange(intent.text)
            is BookmarkDiaryIntent.OnCompleteClick -> onCompleteClick(intent.expression, intent.translatedExpression)
            is BookmarkDiaryIntent.OnBackClick -> onBackClick()
        }
    }

    private fun onDiaryTextChange(text: TextFieldValue) = intent {
        reduce {
            state.copy(
                diaryText = text,
                isValidDiary = text.text.isNotBlank()
            )
        }
    }

    private fun onCompleteClick(expression: String, translatedExpression: String) = intent {
        if (!state.isValidDiary) return@intent

        reduce { state.copy(isLoading = true, error = null) }

        val engKorExpression = "$expression $translatedExpression"
        val dto = WriteDiaryRequestDto(
            content = state.diaryText.text,
            engKorExpression = engKorExpression
        )

        try {
            val response = diaryRepository.postDiary(dto)
            val data = response.data()
            reduce { state.copy(isLoading = false) }
            postSideEffect(BookmarkDiarySideEffect.NavigateToHomeWithSuccess(data.diaryId, data.retrievedBadgeList))
        } catch (t: Throwable) {
            reduce { state.copy(isLoading = false, error = t.message) }
            postSideEffect(BookmarkDiarySideEffect.ShowError(t.message ?: "일기 저장 중 오류가 발생했습니다."))
        }
    }

    private fun onBackClick() = intent {
        postSideEffect(BookmarkDiarySideEffect.NavigateBack)
    }
}
