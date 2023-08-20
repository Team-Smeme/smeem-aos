package com.sopt.smeem.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Anonymous
import com.sopt.smeem.SmeemException
import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Training
import com.sopt.smeem.domain.model.TrainingGoal
import com.sopt.smeem.domain.repository.TrainingRepository
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTrainingVM @Inject constructor(
    private val userRepository: UserRepository,
    @Anonymous private val trainingRepository: TrainingRepository
) : ViewModel() {
    private val _selectedGoal = MutableLiveData<TrainingGoalType>()
    val selectedGoal: LiveData<TrainingGoalType>
        get() = _selectedGoal

    private val _trainingGoal = MutableLiveData<TrainingGoal>()
    val trainingGoal: LiveData<TrainingGoal>
        get() = _trainingGoal

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

    fun setSelectedGoal(goal: TrainingGoalType) {
        _selectedGoal.value = goal
    }

    fun getGoalDetail(onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            trainingRepository.getDetail(_selectedGoal.value)
                .onSuccess { _trainingGoal.value = it }
                .onHttpFailure { e -> onError(e) }
        }
    }

    fun sendServer(onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            userRepository.editTraining(
                training = Training(type = selectedGoal.value!!)
            ).onHttpFailure { e -> onError(e) }
        }
    }

}