package com.sopt.smeem.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Authenticated
import com.sopt.smeem.domain.model.DiarySummaries
import com.sopt.smeem.domain.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Authenticated private val diaryRepository: DiaryRepository,
) : ViewModel() {

    private val _responseDiaries: MutableLiveData<DiarySummaries> = MutableLiveData()
    val responseDiaries: LiveData<DiarySummaries>
        get() = _responseDiaries

    fun getDiaries(start: String, end: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                diaryRepository.getDiaries(start, end)
            }.fold({
                _responseDiaries.value = it.getOrNull()
            }, {
                Log.e("message", it.message.toString())
            })
        }
    }
}
