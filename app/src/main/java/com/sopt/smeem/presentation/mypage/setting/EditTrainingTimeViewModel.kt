package com.sopt.smeem.presentation.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.Training
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditTrainingTimeViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _trainingTime =
        MutableStateFlow<TrainingTime>(TrainingTime(setOf(), DEFAULT_HOUR, DEFAULT_MINUTE))
    val trainingTime: StateFlow<TrainingTime> = _trainingTime.asStateFlow()

    // 서버로 보내기 위한 선택시간 저장 변수
    private val _days = MutableStateFlow(trainingTime.value.days.toMutableSet())
    val days: StateFlow<MutableSet<Day>> = _days.asStateFlow()

    private val _hour = MutableStateFlow(trainingTime.value.hour)
    val hour: StateFlow<Int> = _hour.asStateFlow()

    private val _minute = MutableStateFlow(trainingTime.value.minute)
    val minute: StateFlow<Int> = _minute.asStateFlow()

    fun initialize(newTrainingTime: TrainingTime) {
        _trainingTime.value = newTrainingTime
        _days.value = newTrainingTime.days.toMutableSet()
        _hour.value = newTrainingTime.hour
        _minute.value = newTrainingTime.minute
        Timber.e("initialize: ${_trainingTime.value}")
    }

    fun isDaySelected(content: String) = _days.value.contains(Day.from(content))
    fun addDay(content: String) = _days.value.add(Day.from(content))
    fun removeDay(content: String) = _days.value.remove(Day.from(content))

    fun toggleDaySelection(day: Day) {
        _days.value = if (_days.value.contains(day)) {
            _days.value.toMutableSet().apply { remove(day) }
        } else {
            _days.value.toMutableSet().apply { add(day) }
        }
    }

    fun updateHourMinute(hour: Int, minute: Int) {
        _hour.value = hour
        _minute.value = minute
    }

    fun canConfirmEdit() = _days.value.isNotEmpty() &&
            TrainingTime(_days.value, hour.value, minute.value) != trainingTime.value

    fun sendServer(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.editTraining(
                    Training(
                        trainingTime = TrainingTime(
                            _days.value,
                            hour.value,
                            minute.value
                        )
                    )
                )
                onSuccess()
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    companion object {
        const val DEFAULT_HOUR = 22
        const val DEFAULT_MINUTE = 0
    }
}