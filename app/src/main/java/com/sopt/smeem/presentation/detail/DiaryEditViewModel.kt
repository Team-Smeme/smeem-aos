package com.sopt.smeem.presentation.detail

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class DiaryEditViewModel(): ViewModel() {
    val diary = MutableLiveData("")
    val isValidDiary: LiveData<Boolean> = diary.map { isValidDiaryFormat(it) }

    private fun isValidDiaryFormat(diary: String): Boolean {
        if (diary.replace("[^a-zA-Z]".toRegex(), "").isNotEmpty()) {
            return true
        }
        return false
    }
}