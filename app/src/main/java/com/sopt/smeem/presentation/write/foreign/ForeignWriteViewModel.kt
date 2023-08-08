package com.sopt.smeem.presentation.write.foreign

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
class ForeignWriteViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    var topicId: Long = -1

    val topic = MutableLiveData("")
    val diary = MutableLiveData("")
    val isValidDiary: LiveData<Boolean> = diary.map { isValidDiaryFormat(it) }

    fun getRandomTopic(onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            diaryRepository.getTopic()
                .onSuccess {
                    topicId = it.id
                    topic.value = it.content
                }
                .onHttpFailure { e -> onError(e) }
        }
    }

    fun uploadDiary(onSuccess: (List<RetrievedBadge>) -> Unit, onError: (SmeemException) -> Unit) {
        Log.d("topic id", "$topicId")
        if (topicId != (-1).toLong()) {
            Log.d("with random topic", "with random topic")
            diaryWithTopic(onSuccess, onError)
        } else {
            Log.d("without random topic", "without random topic")
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