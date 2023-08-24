package com.sopt.smeem.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.MyPage
import com.sopt.smeem.domain.model.PushAlarm
import com.sopt.smeem.domain.repository.LocalRepository
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
internal class MyPageVM @Inject constructor(
    private val localRepository: LocalRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _response: MutableLiveData<MyPage> = MutableLiveData<MyPage>()
    val response: LiveData<MyPage>
        get() = _response

    var isTimeSet: Boolean = false
    var days: MutableSet<Day> = mutableSetOf()

    fun getData(onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            userRepository.getMyPage().apply {
                this.onSuccess {
                    _response.value = it
                    isTimeSet = it.hasPushAlarm && it.trainingTime.isSet()
                }
                this.onHttpFailure { onError(it) }
            }
        }
    }

    fun changePushAlarm(hasAlarm: Boolean, onError: (SmeemException) -> Unit) {
        viewModelScope.launch {
            userRepository.editPushAlarm(
                push = PushAlarm(hasAlarm = hasAlarm)
            ).onHttpFailure { e -> onError(e) }
        }
    }

    fun isDaySelected(content: String) = days.contains(Day.from(content))

    fun clearLocal() {
        runBlocking {
            localRepository.clear()
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            userRepository.deleteUser()
        }

        runBlocking {
            localRepository.clear()
        }
    }
}