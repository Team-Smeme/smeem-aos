package com.sopt.smeem.presentation.mypage.setting

import androidx.lifecycle.MutableLiveData
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
import javax.inject.Inject

@HiltViewModel
class EditTrainingTimeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var originalTime: TrainingTime = TrainingTime(
        setOf(Day.MON, Day.TUE, Day.WED, Day.THU, Day.FRI),
        DEFAULT_HOUR,
        DEFAULT_MINUTE
    )

    // 서버로 보내기 위한 선택시간 저장 변수
    private val _days = MutableStateFlow(originalTime.days.toMutableSet())
    val days: StateFlow<MutableSet<Day>> = _days.asStateFlow()
    var hour = MutableLiveData<Int>()
    var minute = MutableLiveData<Int>()

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

    fun canConfirmEdit() =
        _days.value.isNotEmpty() && (TrainingTime(
            _days.value,
            hour.value!!,
            minute.value!!
        ) != originalTime)

    fun sendServer(onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.editTraining(
                    Training(
                        trainingTime = TrainingTime(
                            _days.value,
                            hour.value!!,
                            minute.value!!
                        )
                    )
                )
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