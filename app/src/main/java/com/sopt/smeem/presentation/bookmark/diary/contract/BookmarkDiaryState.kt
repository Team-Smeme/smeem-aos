package com.sopt.smeem.presentation.bookmark.diary.contract

import androidx.compose.ui.text.input.TextFieldValue

data class BookmarkDiaryState(
    val diaryText: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isValidDiary: Boolean = false
)