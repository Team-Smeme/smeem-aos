package com.sopt.smeem.presentation.mypage.mysummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.data.usecase.MySummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MySummaryViewModel @Inject constructor(
    private val mySummaryUseCase: MySummaryUseCase
) : ContainerHost<MySummaryState, MySummarySideEffect>,
    ViewModel() {

    private val _isDataChanged = MutableStateFlow(false)
    val isDataChanged = _isDataChanged.asStateFlow()

    override val container = container<MySummaryState, MySummarySideEffect>(MySummaryState())

    init {
        fetchMySummaryData()
    }

    fun fetchMySummaryData() {
        viewModelScope.launch {
            intent { reduce { state.copy(uiState = MySummaryUiState.Idle) } }

            val loadingJob = launch {
                delay(500)
                intent { reduce { state.copy(uiState = MySummaryUiState.Loading) } }
            }

            try {
                val (smeemData, planData, badgesData) = mySummaryUseCase()
                loadingJob.cancel()

                intent {
                    reduce {
                        state.copy(uiState = MySummaryUiState.Success(smeemData, planData, badgesData))
                    }
                }
            } catch (t: Throwable) {
                intent {
                    reduce {
                        state.copy(uiState = MySummaryUiState.Error(t))
                    }
                }
            }
        }
    }

    fun setDataChanged(value: Boolean) {
        _isDataChanged.value = value
    }
}