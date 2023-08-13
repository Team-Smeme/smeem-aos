package com.sopt.smeem.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.data.model.response.DiaryWritingResponse
import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.DiarySummaries
import com.sopt.smeem.domain.model.DiarySummary
import com.sopt.smeem.domain.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
) : ViewModel() {

    val badge = MutableLiveData<DiaryWritingResponse.RetrievedBadgeResponse?>()

    private val _responseDiaries: MutableLiveData<DiarySummaries> = MutableLiveData()
    val responseDiaries: LiveData<DiarySummaries>
        get() = _responseDiaries

    private val _responseDateDiary: MutableLiveData<DiarySummary?> = MutableLiveData()
    val responseDateDiary: LiveData<DiarySummary?> get() = _responseDateDiary

    fun getDiaries(start: String, end: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                diaryRepository.getDiaries(start, end)
            }.fold({
                Log.e("message", it.getOrNull().toString())
                _responseDiaries.value = it.getOrNull()
            }, {
                Log.e("message", it.message.toString())
            })
        }
    }

    fun getDateDiary(date: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                diaryRepository.getDiaries(date, date)
            }.fold({
                _responseDateDiary.value = it.getOrNull()?.diaries?.values?.firstOrNull()
            }, {
                Log.e("message", it.message.toString())
            })
        }
    }
}
