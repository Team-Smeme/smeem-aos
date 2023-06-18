package com.sopt.smeem.presentation.auth.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Authenticated
import com.sopt.smeem.Day
import com.sopt.smeem.SmeemException
import com.sopt.smeem.SocialType
import com.sopt.smeem.StudyGoal
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Authentication
import com.sopt.smeem.domain.model.LoginResult
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.OnBoardingGoal
import com.sopt.smeem.domain.repository.AuthRepository
import com.sopt.smeem.domain.repository.LoginRepository
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingVM @Inject constructor() : ViewModel() {
    @Inject
    @Authenticated(false)
    lateinit var loginRepository: LoginRepository

    @Inject
    @Authenticated
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var authRepository: AuthRepository

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult

    private val _selectedGoal = MutableLiveData<StudyGoal>()
    val selectedGoal: LiveData<StudyGoal>
        get() = _selectedGoal

    private val _onBoardingGoal = MutableLiveData<OnBoardingGoal>()
    val onBoardingGoal: LiveData<OnBoardingGoal>
        get() = _onBoardingGoal

    private val _setTimeLater = MutableLiveData<Boolean>()
    val setTimeLater: LiveData<Boolean>
        get() = _setTimeLater

    private val _goAnonymous = MutableLiveData<Boolean>()
    val goAnonymous: LiveData<Boolean>
        get() = _goAnonymous

    private val _alreadyHasToken = MutableLiveData<Boolean>(false)
    val alreadyHasToken: LiveData<Boolean>
        get() = _alreadyHasToken


    var step: Int = 0
    val days = mutableListOf<Day>()
    var hour: Int = 0
    var minute: Int = 0

    fun goToNext() {
        step++
    }

    fun checkToken() {
        viewModelScope.launch {
            _alreadyHasToken.value = authRepository.isAuthenticated()
        }
    }

    fun isDaySelected(content: String) = days.contains(Day.from(content))
    fun addDay(content: String) = days.add(Day.from(content))
    fun removeDay(content: String) = days.remove(Day.from(content))
    fun upsert(target: StudyGoal) {
        if (selectedGoal.value == target) {
            _selectedGoal.value!!.selected = false
            _selectedGoal.value = StudyGoal.NONE
        } else {
            _selectedGoal.value = target
            _selectedGoal.value!!.selected = true
        }
    }

    fun none() {
        _selectedGoal.value = StudyGoal.NONE
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
            userRepository.patchOnBoarding(
                OnBoarding(
                    studyGoal = selectedGoal.value ?: StudyGoal.NO_IDEA,
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
        // temp
        _onBoardingGoal.value = OnBoardingGoal(
            goal = "외국어 업무 문서 읽고 작성하기",
            howTo = """
                >주 3회 이상 일기 작성하기
                >편지글 형태의 일기 작성하기
            """.trimMargin(">"),
            goalDetail = """
                >사전 보지 않고 일기 작성하기
                >TOEIC 단어책 뭐뭐하기
            """.trimMargin(">")

        )
        /*
        TODO : server spec 맞춰지면
        viewModelScope.launch {
            goalRepository.getGoalDetail(selectedGoal.value!!)
                .onSuccess { _onBoardingGoal.value = it }
                .onHttpFailure { e -> onError(e) }
        }*/
    }
}