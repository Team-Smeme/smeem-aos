package com.sopt.smeem.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.SmeemException
import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Training
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTrainingVM @Inject constructor() : ViewModel() {
    @Inject
    lateinit var userRepository: UserRepository

    private val _selectedGoal = MutableLiveData<TrainingGoalType>()
    val selectedGoal: LiveData<TrainingGoalType>
        get() = _selectedGoal

    fun upsert(target: TrainingGoalType) {
        if (selectedGoal.value == target) {
            _selectedGoal.value!!.selected = false
            _selectedGoal.value = TrainingGoalType.NO_SELECTED
        } else {
            _selectedGoal.value = target
            _selectedGoal.value!!.selected = true
        }
    }

    fun none() {
        _selectedGoal.value = TrainingGoalType.NO_SELECTED
    }

    fun sendServer(onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            userRepository.editTraining(
                Training(
                    type = selectedGoal.value!!,
                )
            ).onHttpFailure { e -> onError(e) }
        }
    }

}