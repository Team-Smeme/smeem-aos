package com.sopt.smeem.presentation.write.natiive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NativeWriteStep1ViewModel @Inject constructor() : ViewModel() {
    val topic = MutableLiveData("")
    val diary = MutableLiveData("")
    val isValidDiary: LiveData<Boolean> = diary.map { isValidDiaryFormat(it) }

    private fun isValidDiaryFormat(diary: String): Boolean {
        if (diary.filterNot { it.isWhitespace() }.isNotEmpty()) {
            return true
        }
        return false
    }
}