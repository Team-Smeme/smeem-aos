package com.sopt.smeem.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.util.DateUtil
import com.sopt.smeem.util.TextUtil.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    val topic = MutableLiveData<String?>()
    val diary = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val writer = MutableLiveData<String>()

    val isTopicExist: LiveData<Boolean> = topic.map { it != "" }

    fun getDiaryDetail(onError: (SmeemException) -> Unit) {
        // TODO: HomeActivity에서 diaryId 받아오기
        viewModelScope.launch {
            diaryRepository.getDiaryDetail(500)
                .onSuccess {
                    topic.value = it.topic
                    diary.value = it.content
                    date.value = it.createdAt?.toLocalDateTime()
                        ?.let { date -> DateUtil.asString(date) }
                    writer.value = it.username
                }
                .onHttpFailure { e -> onError(e) }
        }
    }
}