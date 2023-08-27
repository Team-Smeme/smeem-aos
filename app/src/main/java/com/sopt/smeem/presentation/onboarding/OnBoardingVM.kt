package com.sopt.smeem.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.sopt.smeem.Anonymous
import com.sopt.smeem.SmeemException
import com.sopt.smeem.SocialType
import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Authentication
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.LoginResult
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.Training
import com.sopt.smeem.domain.model.TrainingGoal
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.domain.repository.LocalRepository
import com.sopt.smeem.domain.repository.LoginRepository
import com.sopt.smeem.domain.repository.TrainingRepository
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingVM @Inject constructor(
    @Anonymous private val loginRepository: LoginRepository,
    @Anonymous private val trainingRepository: TrainingRepository,
    @Anonymous private val userRepositoryWithAnonymous: UserRepository,
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult

    private val _selectedGoal = MutableLiveData(TrainingGoalType.NO_SELECTED)
    val selectedGoal: LiveData<TrainingGoalType>
        get() = _selectedGoal

    private val _trainingGoal = MutableLiveData<TrainingGoal>()
    val trainingGoal: LiveData<TrainingGoal>
        get() = _trainingGoal

    private val _setTimeLater = MutableLiveData<Boolean>()
    val setTimeLater: LiveData<Boolean>
        get() = _setTimeLater

    private val _isNotiGranted = MutableLiveData<Boolean>()
    val isNotiGranted: LiveData<Boolean>
        get() = _isNotiGranted

    private val _goAnonymous = MutableLiveData<Boolean>()
    val goAnonymous: LiveData<Boolean>
        get() = _goAnonymous

    private val _step = MutableLiveData<Int>(1)
    val step: LiveData<Int>
        get() = _step

    private val _onLoading = MutableLiveData(LoadingState.NOT_STARTED)
    val onLoading: LiveData<LoadingState>
        get() = _onLoading

    // selected time
    val selectedHour = MutableLiveData(DEFAULT_HOUR)
    val selectedMinute = MutableLiveData(DEFAULT_MINUTE)

    val days = mutableListOf<Day>()
    val isDaysEmpty = MutableLiveData(false)

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
                // 회원가입 바텀시트 누른 후 뒤로가기할 때
            if (step.value == 4) {
                _step.value?.minus(2)
            } else {
                step.value?.minus(1)?.let { backedStep ->
                    if (backedStep < 0) 0 else backedStep // 0 보다 작을 수 없다.
                } ?: 1 // null 일 경우 1로 세팅
            }
    }

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
        setNotiPermissionStatus(false)
    }

    fun setNotiPermissionStatus(status: Boolean) {
        _isNotiGranted.value = status
    }

    fun login(
        kakaoAccessToken: String,
        kakaoRefreshToken: String,
        socialType: SocialType,
        onError: (SmeemException) -> Unit
    ) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                viewModelScope.launch {
                    loginRepository.execute(accessToken = kakaoAccessToken, socialType, it.result)
                        .onSuccess {
                            _loginResult.value = it
                        }
                        .onHttpFailure { e -> onError(e) }
                }
            }
        }
    }

    fun goAnonymous() {
        _goAnonymous.value = true
    }

    fun saveOnBoardingData() {
        // TODO : Room 에 보관하기
    }

    fun sendPlanDataOnAnonymous(onSuccess: (Unit) -> Unit, onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            userRepositoryWithAnonymous.registerOnBoarding(
                OnBoarding(
                    trainingGoalType = selectedGoal.value ?: TrainingGoalType.NO_SELECTED,
                    hasAlarm = isNotiGranted.value ?: false,
                    day = days,
                    hour = selectedHour.value,
                    minute = selectedMinute.value
                ),
                loginResult.value!!
            )
                .onSuccess(onSuccess)
                .onHttpFailure { e -> onError(e) }
            loadingEnd()
        }
    }

    fun sendPlanDataWithAuth(
        token: String,
        onSuccess: (Unit) -> Unit,
        onError: (SmeemException) -> Unit
    ) {
        viewModelScope.launch {
            userRepositoryWithAnonymous.editTraining(
                accessToken = token,
                training = Training(
                    type = selectedGoal.value ?: TrainingGoalType.NO_SELECTED,
                    trainingTime = TrainingTime(days = days.toSet(), hour = hour, minute = minute),
                    hasAlarm = isNotiGranted.value ?: false,
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

    fun loadingStart() {
        _onLoading.value = LoadingState.ACT
    }

    fun loadingEnd() {
        _onLoading.value = LoadingState.DONE
    }

    fun saveTokenOnLocal(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            localRepository.setAuthentication(
                Authentication(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                )
            )
        }
    }

    companion object {
        const val DEFAULT_HOUR = 22
        const val DEFAULT_MINUTE = 0
    }
}