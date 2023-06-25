package com.sopt.smeem.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sopt.smeem.TrainingGoalType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditTrainingVM @Inject constructor() : ViewModel() {
    private val _selectedGoal = MutableLiveData<TrainingGoalType>()
    val selectedGoal: LiveData<TrainingGoalType>
        get() = _selectedGoal

    fun upsert(target: TrainingGoalType) {
        if (selectedGoal.value == target) {
            _selectedGoal.value!!.selected = false
            _selectedGoal.value = TrainingGoalType.NO_IDEA
        } else {
            _selectedGoal.value = target
            _selectedGoal.value!!.selected = true
        }
    }

    fun none() {
        _selectedGoal.value = TrainingGoalType.NO_IDEA
    }

}