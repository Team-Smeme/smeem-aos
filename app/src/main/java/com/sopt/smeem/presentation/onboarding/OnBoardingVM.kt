package com.sopt.smeem.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Authenticated
import com.sopt.smeem.SmeemException
import com.sopt.smeem.SocialType
import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Authentication
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.LoginResult
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.TrainingGoal
import com.sopt.smeem.domain.repository.AuthRepository
import com.sopt.smeem.domain.repository.LoginRepository
import com.sopt.smeem.domain.repository.TrainingRepository
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingVM @Inject constructor(
    @Authenticated(false) private val loginRepository: LoginRepository,
    @Authenticated private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val trainingRepository: TrainingRepository,
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult

    private val _selectedGoal = MutableLiveData<TrainingGoalType>()
    val selectedGoal: LiveData<TrainingGoalType>
        get() = _selectedGoal

    private val _trainingGoal = MutableLiveData<TrainingGoal>()
    val trainingGoal: LiveData<TrainingGoal>
        get() = _trainingGoal

    private val _setTimeLater = MutableLiveData<Boolean>()
    val setTimeLater: LiveData<Boolean>
        get() = _setTimeLater

    private val _goAnonymous = MutableLiveData<Boolean>()
    val goAnonymous: LiveData<Boolean>
        get() = _goAnonymous

    private val _step = MutableLiveData<Int>(1)
    val step: LiveData<Int>
        get() = _step

    // selected time
    val selectedHour = MutableLiveData<Int>(22)
    val selectedMinute = MutableLiveData<Int>(0)

    val days = mutableListOf<Day>()
    var hour: Int = 0
    var minute: Int = 0

    fun nextStep() {
        _step.value =
            _step.value?.plus(1)?.let { toBeNextStep ->
                if (toBeNextStep > 4) 4 else toBeNextStep // 4 는 api token check 용, 4 보다 클 수 없다.
            } ?: 2
    }

    fun backStep() {
        _step.value =
            step.value?.minus(1)?.let { backedStep ->
                if (backedStep < 1) 1 else backedStep // 1 보다 작을 수 없다.
            } ?: 1 // null 일 경우 1로 세팅

    }

    fun alreadyAuthed() = viewModelScope.async { authRepository.isAuthenticated() }.getCompleted()

    fun isDaySelected(content: String) = days.contains(Day.from(content))
    fun addDay(content: String) = days.add(Day.from(content))
    fun removeDay(content: String) = days.remove(Day.from(content))

    fun formatHour(hour: Int): String {
        if (hour in 13..24) {
            return "%02d".format(hour - 12)
        } else if (hour == 0) {
            return "12"
        }
        return "%02d".format(hour)
    }

    fun formatMinute(minute: Int): String = "%02d".format(minute)
    fun getAmPm(hour: Int): String {
        if (hour in 12..23) {
            return " PM"
        }
        return " AM"
    }

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

    fun timeLater() {
        _setTimeLater.value = true
    }

    fun login(
        kakaoAccessToken: String,
        kakaoRefreshToken: String,
        socialType: SocialType,
        onError: (SmeemException) -> Unit
    ) {
        viewModelScope.launch {
            loginRepository.execute(accessToken = kakaoAccessToken, socialType)
                .onSuccess {
                    // save on local storage
                    authRepository.setAuthentication(
                        Authentication(
                            accessToken = it.apiAccessToken,
                            refreshToken = it.apiRefreshToken
                        )
                    )

                    _loginResult.value = it
                }
                .onHttpFailure { e -> onError(e) }
        }
    }

    fun goAnonymous() {
        _goAnonymous.value = true
    }

    fun saveOnBoardingData() {
        // TODO : Room 에 보관하기
    }

    fun sendPlanData(onSuccess: (Unit) -> Unit, onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            userRepository.registerOnBoarding(
                OnBoarding(
                    trainingGoalType = selectedGoal.value ?: TrainingGoalType.NO_SELECTED,
                    // TODO: 알림 권한에 동의했을 때는?
                    hasAlarm = setTimeLater.value?.not() ?: false,
                    day = days,
                    hour = hour,
                    minute = minute
                )
            )
                .onSuccess(onSuccess)
                .onHttpFailure { e -> onError(e) }
        }
    }

    fun getGoalDetail(onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            trainingRepository.getDetail(selectedGoal.value)
                .onSuccess { _trainingGoal.value = it }
                .onHttpFailure { e -> onError(e) }
        }
    }
}