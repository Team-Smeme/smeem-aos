package com.sopt.smeem.presentation.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Anonymous
import com.sopt.smeem.domain.dto.TrainingPlanDto
import com.sopt.smeem.domain.model.Training
import com.sopt.smeem.domain.repository.TrainingRepository
import com.sopt.smeem.domain.repository.UserRepository
import com.sopt.smeem.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTrainingPlanViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @Anonymous private val trainingRepository: TrainingRepository
) : ViewModel() {

    private val _trainingPlans = MutableStateFlow<UiState<List<TrainingPlanDto>>>(UiState.Loading)
    val trainingPlans: StateFlow<UiState<List<TrainingPlanDto>>> = _trainingPlans.asStateFlow()

    init {
        getTrainingPlans()
    }

    private fun getTrainingPlans() {
        viewModelScope.launch {
            try {
                val response = trainingRepository.getPlans()
                response.data().let { dto ->
                    _trainingPlans.value = UiState.Success(dto)
                }
            } catch (t: Throwable) {
                _trainingPlans.value = UiState.Failure(t)
            }
        }
    }

    fun editTrainingPlan(planId: Int, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.editTraining(
                    Training(
                        planId = planId
                    )
                )
                onSuccess()
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}