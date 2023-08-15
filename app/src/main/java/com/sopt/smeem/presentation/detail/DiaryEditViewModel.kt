package com.sopt.smeem.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Diary
import com.sopt.smeem.domain.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryEditViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    var diaryId: Long? = -1

    val diary = MutableLiveData<String>()
    val isValidDiary: LiveData<Boolean> = diary.map { isValidDiaryFormat(it) }

    fun editDiary(onSuccess: (Unit) -> Unit, onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            diaryRepository.patchDiary(
                Diary(
                    id = diaryId,
                    content = diary.value!!
                )
            )
                .onSuccess(onSuccess)
                .onHttpFailure { e -> onError(e) }
        }
    }

    private fun isValidDiaryFormat(diary: String): Boolean {
        if (diary.replace("[^a-zA-Z]".toRegex(), "").isNotEmpty()) {
            return true
        }
        return false
    }
}