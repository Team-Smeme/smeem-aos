package com.sopt.smeem.presentation.write.natiive

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Diary
import com.sopt.smeem.domain.model.RetrievedBadge
import com.sopt.smeem.domain.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NativeWriteStep2ViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    var topicId: Long = -1

    val diary = MutableLiveData("")
    val isValidDiary: LiveData<Boolean> = diary.map { isValidDiaryFormat(it) }

    fun uploadDiary(onSuccess: (List<RetrievedBadge>) -> Unit, onError: (SmeemException) -> Unit) {
        if (topicId != (-1).toLong()) {
            diaryWithTopic(onSuccess, onError)
        } else {
            diaryWithoutTopic(onSuccess, onError)
        }
    }

    private fun diaryWithTopic(onSuccess: (List<RetrievedBadge>) -> Unit, onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            diaryRepository.postDiary(
                Diary(
                    topicId = topicId,
                    content = diary.value!!
                )
            )
                .onSuccess(onSuccess)
                .onHttpFailure { e -> onError(e) }
        }
    }

    private fun diaryWithoutTopic(onSuccess: (List<RetrievedBadge>) -> Unit, onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            diaryRepository.postDiary(
                Diary(
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