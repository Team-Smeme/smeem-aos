package com.sopt.smeem.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sopt.smeem.domain.model.DiarySummaries
import com.sopt.smeem.domain.model.DiarySummary
import com.sopt.smeem.domain.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
) : ViewModel() {
    lateinit var nowBadge: String

    var isFirstBadge: Boolean = true
    val badgeName = MutableLiveData<String>()
    val badgeImageUrl = MutableLiveData<String>()

    private val _responseDiaries: MutableLiveData<DiarySummaries> = MutableLiveData()
    val responseDiaries: LiveData<DiarySummaries>
        get() = _responseDiaries

    private val _responseDateDiary: MutableLiveData<DiarySummary?> = MutableLiveData()
    val responseDateDiary: LiveData<DiarySummary?> get() = _responseDateDiary

    suspend fun getDiaries(start: String, end: String) {
        kotlin.runCatching {
            diaryRepository.getDiaries(start, end)
        }.fold({
            Log.e("message", it.getOrNull().toString())
            _responseDiaries.value = it.getOrNull()
        }, {
            Log.e("message", it.message.toString())
        })
    }

    suspend fun getDateDiary(date: String) {
        kotlin.runCatching {
            diaryRepository.getDiaries(start = date, end = date)
        }.fold({
            _responseDateDiary.value = it.getOrNull()?.diaries?.values?.firstOrNull()
        }, {
            Log.e("message", it.message.toString())
        })
    }

    fun setBadgeInfo(name: String, imageUrl: String, isFirst: Boolean) {
        badgeName.value = name
        badgeImageUrl.value = imageUrl
        isFirstBadge = isFirst

        nowBadge = name
    }
}
