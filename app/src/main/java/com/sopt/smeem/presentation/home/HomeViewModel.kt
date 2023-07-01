package com.sopt.smeem.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Authenticated
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Date
import com.sopt.smeem.domain.model.DiarySummary
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    @Inject
    @Authenticated
    lateinit var diaryRepository: DiaryRepository

    private val _flag = MutableLiveData<Date>()
    val flag : LiveData<Date>
        get() = _flag

    private val _diarySummaries = MutableLiveData<Map<Date, DiarySummary>>()
    val diarySummaries: LiveData<Map<Date, DiarySummary>>
        get() = _diarySummaries

    private val _has30Past = MutableLiveData<Boolean>()
    val has30Past: LiveData<Boolean>
        get() = _has30Past

    fun getContents(
        day: LocalDateTime,
        onError: (SmeemException) -> Unit
    ) {
        _flag.value = Date.from(LocalDateTime.now())
        viewModelScope.launch {
            diaryRepository.getDiaries(
                start = DateUtil.yyyy_mm_dd(day.minusMonths(1)),
                end = DateUtil.yyyy_mm_dd(day.plusMonths(1))
            )
                .onSuccess {
                    _diarySummaries.value = it.diaries
                    _has30Past.value = it.has30Past
                }
                .onHttpFailure { e -> onError(e) }
        }
    }

}