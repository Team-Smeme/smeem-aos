package com.sopt.smeem.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashVM @Inject constructor(
    private val localRepository: LocalRepository,
) : ViewModel() {
    private var _isAuthed = MutableLiveData<Boolean>()
    val isAuthed: LiveData<Boolean>
        get() = _isAuthed

    fun checkAuthed() {
        viewModelScope.launch {
            delay(1500L) // non-blocking
            _isAuthed.value = localRepository.isAuthenticated()
        }
    }
}