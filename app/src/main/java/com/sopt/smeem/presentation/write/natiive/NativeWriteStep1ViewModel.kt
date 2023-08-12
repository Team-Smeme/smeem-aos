package com.sopt.smeem.presentation.write.natiive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Anonymous
import com.sopt.smeem.domain.repository.TranslateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NativeWriteStep1ViewModel @Inject constructor(
    @Anonymous private val translateRepository: TranslateRepository,
) : ViewModel() {
    val topic = MutableLiveData("")
    val diary = MutableLiveData("")
    val isValidDiary: LiveData<Boolean> = diary.map { isValidDiaryFormat(it) }

    private val _translateResult = MutableLiveData<String>()
    val translateResult: LiveData<String>
        get() = _translateResult

    private fun isValidDiaryFormat(diary: String): Boolean {
        if (diary.filterNot { it.isWhitespace() }.isNotEmpty()) {
            return true
        }
        return false
    }

    fun translate() {
        viewModelScope.launch {
            _translateResult.value = translateRepository.execute(diary.value ?: "").translateResult
        }
    }
}