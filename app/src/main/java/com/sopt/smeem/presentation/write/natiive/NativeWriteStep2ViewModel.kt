package com.sopt.smeem.presentation.write.natiive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.data.SmeemDataStore
import com.sopt.smeem.domain.dto.RetrievedBadgeDto
import com.sopt.smeem.domain.dto.WriteDiaryRequestDto
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.domain.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class NativeWriteStep2ViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val localRepository: LocalRepository,
) : ViewModel() {
    var topicId: Long = -1

    val diary = MutableLiveData("")
    val isValidDiary: LiveData<Boolean> = diary.map { isValidDiaryFormat(it) }

    fun uploadDiary(
        onSuccess: (Long, List<RetrievedBadgeDto>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val selectedTopicId = if (topicId < 0) null else topicId

        diary.value?.let { content ->
            viewModelScope.launch {
                launch {
                    requestToServer(
                        WriteDiaryRequestDto(content, selectedTopicId),
                        onSuccess,
                        onError
                    )
                }
                launch { updateRecentDiaryDateOnLocal() }
            }
        }
    }

    private suspend fun requestToServer(
        dto: WriteDiaryRequestDto,
        onSuccess: (Long, List<RetrievedBadgeDto>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            diaryRepository.postDiary(dto)
                .run { onSuccess(data().diaryId, data().retrievedBadgeList) }
        } catch (t: Throwable) {
            onError(t)
        }
    }

    private suspend fun updateRecentDiaryDateOnLocal() {
        // recent_diary_date 값 변경
        localRepository.setStringValue(
            SmeemDataStore.RECENT_DIARY_DATE, LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
    }

    private fun isValidDiaryFormat(diary: String): Boolean {
        if (diary.replace("[^a-zA-Z]".toRegex(), "").isNotEmpty()) {
            return true
        }
        return false
    }
}
