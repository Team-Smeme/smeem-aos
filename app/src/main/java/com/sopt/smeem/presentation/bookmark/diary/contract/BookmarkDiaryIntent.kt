package com.sopt.smeem.presentation.bookmark.diary.contract

import androidx.compose.ui.text.input.TextFieldValue

sealed class BookmarkDiaryIntent {
    data class OnDiaryTextChange(val text: TextFieldValue) : BookmarkDiaryIntent()
    data class OnCompleteClick(val expression: String, val translatedExpression: String) : BookmarkDiaryIntent()
    object OnBackClick : BookmarkDiaryIntent()
}