package com.sopt.smeem.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Authenticated
import com.sopt.smeem.Day
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.MyPage
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MyPageVM @Inject constructor() : ViewModel() {

    @Inject
    @Authenticated
    lateinit var userRepository: UserRepository

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
                    days.addAll(it.trainingTime.days)
                }
                this.onHttpFailure { onError(it) }
            }
        }
    }

    fun isDaySelected(content: String) = days.contains(Day.from(content))
    fun addDay(content: String) = days.add(Day.from(content))
    fun removeDay(content: String) = days.remove(Day.from(content))
}