package com.sopt.smeem.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.util.TextUtil.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
) : ViewModel() {
    var diaryId: Long? = -1

    val topic = MutableLiveData<String?>()
    val diary = MutableLiveData<String>()
    private val _date = MutableLiveData<LocalDateTime>()
    val date: LiveData<LocalDateTime> = _date
    val dateForUI = MutableLiveData<String>()
    val writer = MutableLiveData<String?>()

    val isTopicExist: LiveData<Boolean> = topic.map { it != "" }

    val isDiaryDeleted = MutableLiveData(false)

    fun getDiaryDetail(onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            diaryRepository.getDiaryDetail(diaryId!!)
                .onSuccess {
                    diaryId = it.id
                    topic.value = it.topic
                    diary.value = it.content
                    _date.value = it.createdAt?.toLocalDateTime()
                    writer.value = it.username
                    dateForUI.value =
                        it.createdAt?.toLocalDateTime()
                            ?.let { date -> com.sopt.smeem.util.DateUtil.asString(date) }
                }
                .onHttpFailure { e -> onError(e) }
        }
    }

    fun deleteDiary(onSuccess: (Unit) -> Unit, onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            diaryId?.let {
                diaryRepository.removeDiary(it)
                    .onSuccess(onSuccess)
                    .onHttpFailure { e -> onError(e) }
            }
        }
    }
}
