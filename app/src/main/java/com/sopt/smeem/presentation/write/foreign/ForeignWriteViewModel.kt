package com.sopt.smeem.presentation.write.foreign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.data.SmeemDataStore.RECENT_DIARY_DATE
import com.sopt.smeem.domain.dto.RetrievedBadgeDto
import com.sopt.smeem.domain.dto.WriteDiaryRequestDto
import com.sopt.smeem.domain.model.LocalStatus
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.domain.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

    fun getRandomTopic(onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                diaryRepository.getTopic().run {
                    topic.value = data().content
                    topicId = data().id
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    fun uploadDiary(onSuccess: (List<RetrievedBadgeDto>) -> Unit, onError: (Throwable) -> Unit) {
        val selectedTopicId = if (topicId < 0) null else topicId

        diary.value?.let {
            viewModelScope.launch {
                requestToServer(WriteDiaryRequestDto(it, selectedTopicId), onSuccess, onError)
                updateRecentDiaryDateOnLocal()
            }
        }
    }

    private suspend fun requestToServer(
        dto: WriteDiaryRequestDto,
        onSuccess: (List<RetrievedBadgeDto>) -> Unit,
        onError: (Throwable) -> Unit
    ) = coroutineScope {
        launch {
            try {
                diaryRepository.postDiary(dto).run { onSuccess(data().retrievedBadgeList) }
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    private suspend fun updateRecentDiaryDateOnLocal() = coroutineScope {
        // recent_diary_date 값 변경
        launch {
            localRepository.setStringValue(
                RECENT_DIARY_DATE, LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            )
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

    private fun isValidDiaryFormat(diary: String): Boolean {
        if (diary.replace("[^a-zA-Z]".toRegex(), "").isNotEmpty()) {
            return true
        }
        return false
    }
}