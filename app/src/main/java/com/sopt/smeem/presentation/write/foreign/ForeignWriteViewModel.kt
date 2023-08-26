package com.sopt.smeem.presentation.write.foreign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.LocalStatus
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Diary
import com.sopt.smeem.domain.model.RetrievedBadge
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.domain.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ForeignWriteViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val localRepository: LocalRepository,
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
        if (topicId != (-1).toLong()) {
            diaryWithTopic(onSuccess, onError)
        } else {
            diaryWithoutTopic(onSuccess, onError)
        }
    }

    fun neverClickedRandomToolTip(): Boolean = runBlocking {
        return@runBlocking localRepository.checkStatus(LocalStatus.RANDOM_TOPIC_TOOL_TIP)
    }

    fun randomTopicTooltipOff() {
        viewModelScope.launch {
            localRepository.saveStatus(LocalStatus.RANDOM_TOPIC_TOOL_TIP)
        }
    }

    private fun diaryWithTopic(
        onSuccess: (List<RetrievedBadge>) -> Unit,
        onError: (SmeemException) -> Unit
    ) {
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

    private fun diaryWithoutTopic(
        onSuccess: (List<RetrievedBadge>) -> Unit,
        onError: (SmeemException) -> Unit
    ) {
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