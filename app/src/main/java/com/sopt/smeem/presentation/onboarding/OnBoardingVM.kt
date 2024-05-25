package com.sopt.smeem.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.sopt.smeem.domain.dto.LoginResultDto
import com.sopt.smeem.domain.dto.PostOnBoardingDto
import com.sopt.smeem.domain.dto.TrainingGoalDto
import com.sopt.smeem.domain.model.Authentication
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.SocialType
import com.sopt.smeem.domain.model.Training
import com.sopt.smeem.domain.model.TrainingGoalType
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.domain.repository.LocalRepository
import com.sopt.smeem.domain.repository.LoginRepository
import com.sopt.smeem.domain.repository.TrainingRepository
import com.sopt.smeem.domain.repository.UserRepository
import com.sopt.smeem.module.Anonymous
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

    private val _loginResult = MutableLiveData<LoginResultDto>()
    val loginResult: LiveData<LoginResultDto>
        get() = _loginResult

    private val _selectedGoal = MutableLiveData(TrainingGoalType.NO_SELECTED)
    val selectedGoal: LiveData<TrainingGoalType>
        get() = _selectedGoal

    private val _trainingGoal = MutableLiveData<TrainingGoalDto>()
    val trainingGoal: LiveData<TrainingGoalDto>
        get() = _trainingGoal

    private val _selectedPlan = MutableLiveData(TrainingPlanType.NOT_SELECTED)
    val selectedPlan: LiveData<TrainingPlanType>
        get() = _selectedPlan

    private val _setTimeLater = MutableLiveData<Boolean>()
    val setTimeLater: LiveData<Boolean>
        get() = _setTimeLater

    private val _isNotiGranted = MutableLiveData<Boolean>()
    val isNotiGranted: LiveData<Boolean>
        get() = _isNotiGranted

    private val _goAnonymous = MutableLiveData<Boolean>()
    val goAnonymous: LiveData<Boolean>
        get() = _goAnonymous

    val _step = MutableLiveData<Int>(1)
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

    fun upsertGoalType(target: TrainingGoalType) {
        if (selectedGoal.value == target) {
            _selectedGoal.value!!.selected = false
            _selectedGoal.value = TrainingGoalType.NO_SELECTED
        } else {
            _selectedGoal.value = target
            _selectedGoal.value!!.selected = true
        }
    }

    fun upsertPlanType(target: TrainingPlanType) {
        if (selectedPlan.value == target) {
            _selectedPlan.value!!.selected = false
            _selectedPlan.value = TrainingPlanType.NOT_SELECTED
        } else {
            _selectedPlan.value = target
            _selectedPlan.value!!.selected = true
        }
    }

    fun noneGoalType() {
        _selectedGoal.value = TrainingGoalType.NO_SELECTED
    }

    fun nonePlanType() {
        _selectedPlan.value = TrainingPlanType.NOT_SELECTED
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
        onError: (Throwable) -> Unit
    ) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { fcmTask ->
            if (fcmTask.isSuccessful) {
                viewModelScope.launch {
                    try {
                        loginRepository.execute(
                            accessToken = kakaoAccessToken,
                            socialType,
                            fcmTask.result
                        ).run {
                            _loginResult.value = data()
                        }
                    } catch (t: Throwable) {
                        onError(t)
                    }
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

    fun sendPlanDataOnAnonymous(onSuccess: (Unit) -> Unit, onError: (Throwable) -> Unit) {
        loginResult.value?.let { loginResult ->
            viewModelScope.launch {
                try {
                    userRepositoryWithAnonymous.registerOnBoarding(
                        PostOnBoardingDto(
                            planId = selectedPlan.value!!.serverId,
                            trainingGoalType = selectedGoal.value ?: TrainingGoalType.NO_SELECTED,
                            hasAlarm = isNotiGranted.value ?: false,
                            day = days,
                            hour = selectedHour.value,
                            minute = selectedMinute.value
                        ), loginResult
                    ).run { onSuccess(Unit) }
                } catch (t: Throwable) {
                    onError(t)
                } finally {
                    loadingEnd()
                }
            }
        }
    }

    fun sendPlanDataWithAuth(
        token: String,
        onSuccess: (Unit) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                userRepositoryWithAnonymous.registerTraining(
                    accessToken = token,
                    training = Training(
                        type = selectedGoal.value ?: TrainingGoalType.NO_SELECTED,
                        trainingTime = TrainingTime(
                            days = days.toSet(),
                            hour = hour,
                            minute = minute
                        ),
                        hasAlarm = isNotiGranted.value ?: false,
                        planId = selectedPlan.value!!.serverId,
                    )
                )
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    fun getGoalDetail(onError: (Throwable) -> Unit) {
        selectedGoal.value?.let { goal ->
            viewModelScope.launch {
                try {
                    trainingRepository.getDetail(goal).run {
                        _trainingGoal.value = data()
                    }
                } catch (t: Throwable) {
                    onError(t)
                }
            }
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