package com.sopt.smeem.presentation.health

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.SmeemErrorCode
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.datasource.HealthChecking
import com.sopt.smeem.data.onHttpFailure
import com.sopt.smeem.data.repository.HealthRepositoryImpl
import com.sopt.smeem.domain.model.health.HealthStatus
import com.sopt.smeem.domain.repository.HealthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

internal class ViewModel(
    private val _result: MutableLiveData<HealthStatus> = MutableLiveData<HealthStatus>(),
    private val healthRepository: HealthRepository = HealthRepositoryImpl(HealthChecking())
) : androidx.lifecycle.ViewModel() {
    val result: LiveData<HealthStatus>
        get() = _result

    fun connect(onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            healthRepository.getHealth().apply {
                this.onSuccess { _result.value = it }
                this.onFailure {
                    when (it) {
                        is HttpException -> this.onHttpFailure(it) { e -> onError.invoke(e) }
                        else -> onError.invoke(SmeemException(SmeemErrorCode.NETWORK_ERROR))
                    }
                }
            }
        }
    }
}