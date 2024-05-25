package com.sopt.smeem.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.data.SmeemDataStore
import com.sopt.smeem.domain.common.SmeemErrorCode
import com.sopt.smeem.domain.common.SmeemException
import com.sopt.smeem.domain.dto.DeleteDiaryRequestDto
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.domain.repository.LocalRepository
import com.sopt.smeem.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val localRepository: LocalRepository,
) : ViewModel() {
    private var diaryId: Long = -1L
    private lateinit var diaryCreatedAt: LocalDateTime

    private val _diaryDetailResult: MutableLiveData<DiaryDetail> = MutableLiveData<DiaryDetail>()
    val diaryDetailResult: LiveData<DiaryDetail>
        get() = _diaryDetailResult

    val isTopicExist: LiveData<Boolean> = _diaryDetailResult.map { !it.topic.isNullOrBlank() }
    val isDiaryDeleted = MutableLiveData(false)

    fun getDiaryId() = diaryDetailResult.value!!.diaryId
    fun getContent() = diaryDetailResult.value!!.content
    fun getTopic() = diaryDetailResult.value!!.topic

    fun setDiaryId(diaryId: Long) {
        if (diaryId < 0) {
            throw SmeemException(SmeemErrorCode.SYSTEM_ERROR, "잘못된 diaryId ($diaryId) 입니다.")
        }
        this.diaryId = diaryId
    }

    fun getDiaryDetail(onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                diaryRepository.getDiaryDetail(diaryId).run {
                    data().let { dto ->
                        _diaryDetailResult.value = DiaryDetail(
                            diaryId = dto.id,
                            topic = dto.topic,
                            content = dto.content,
                            createdAt = DateUtil.asString(dto.createdAt),
                            writerUsername = dto.username
                        )
                        diaryCreatedAt = dto.createdAt

                    }
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    fun deleteDiary(
        onSuccess: (Unit) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            launch { requestToServer(diaryId, onSuccess, onError) }
            // 오늘 작성한 일기일 때만 일기 삭제 시 datastore의 최근 일기 날짜 삭제
            launch { updateRecentDiaryDateOnLocal() }
        }
    }

    private suspend fun requestToServer(
        diaryId: Long,
        onSuccess: (Unit) -> Unit,
        onError: (Throwable) -> Unit
    ) = coroutineScope {
        try {
            diaryRepository.deleteDiary(DeleteDiaryRequestDto(diaryId)).run { onSuccess(Unit) }
        } catch (t: Throwable) {
            onError(t)
        }
    }

    private suspend fun updateRecentDiaryDateOnLocal() = coroutineScope {
        if (diaryCreatedAt.toLocalDate().isEqual(LocalDate.now())) {
            localRepository.remove(SmeemDataStore.RECENT_DIARY_DATE)
            isDiaryDeleted.value = true
        }
    }
}
