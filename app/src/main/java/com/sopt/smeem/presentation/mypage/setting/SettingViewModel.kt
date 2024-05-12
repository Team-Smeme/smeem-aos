package com.sopt.smeem.presentation.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.model.PushAlarm
import com.sopt.smeem.domain.repository.UserRepository
import com.sopt.smeem.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ContainerHost<SettingState, SettingSideEffect>, ViewModel() {

    override val container = container<SettingState, SettingSideEffect>(SettingState())

    init {
        fetchSettingData()
    }

    private fun fetchSettingData() {
        viewModelScope.launch {
            intent { reduce { state.copy(uiState = UiState.Idle) } }

            val loadingJob = launch {
                delay(500)
                intent { reduce { state.copy(uiState = UiState.Loading) } }
            }

            try {
                val response = userRepository.getMyInfo().data()
                loadingJob.cancel()
                intent { reduce { state.copy(uiState = UiState.Success(response)) } }
            } catch (t: Throwable) {
                intent { reduce { state.copy(uiState = UiState.Failure(t)) } }
            }
        }
    }

    fun changePushAlarm(hasAlarm: Boolean, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.editPushAlarm(PushAlarm(hasAlarm = hasAlarm))
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}