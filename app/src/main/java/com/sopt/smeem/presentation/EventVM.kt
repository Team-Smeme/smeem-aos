package com.sopt.smeem.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Smeem.Companion.AMPLITUDE
import com.sopt.smeem.event.AmplitudeEventType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventVM @Inject constructor(): ViewModel() {
    fun sendEvent(event: AmplitudeEventType) {
        viewModelScope.launch {
            AMPLITUDE.track(event.eventName)
        }
    }
}