package com.sopt.smeem.presentation.health

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.health.HealthStatus
import com.sopt.smeem.domain.repository.HealthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ViewModel @Inject constructor() : androidx.lifecycle.ViewModel() {

    @Inject
    lateinit var healthRepository: HealthRepository

    private val _result: MutableLiveData<HealthStatus> = MutableLiveData<HealthStatus>()
    val result: LiveData<HealthStatus>
        get() = _result

    fun connect(onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            healthRepository.getHealth().apply {
                this.onSuccess { _result.value = it }
                this.onHttpFailure { e -> onError(e) }
            }
        }
    }
}