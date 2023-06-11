package com.sopt.smeem.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Day
import com.sopt.smeem.data.datasource.MyPageRetriever
import com.sopt.smeem.data.model.response.MyPageResponse
import com.sopt.smeem.data.repository.MyPageRepositoryImpl
import com.sopt.smeem.domain.repository.MyPageRepository
import kotlinx.coroutines.launch

internal class MyPageVM(
    private val myPageRepository: MyPageRepository = MyPageRepositoryImpl(MyPageRetriever())
) : ViewModel() {
    private val _response: MutableLiveData<MyPageResponse> = MutableLiveData<MyPageResponse>()
    val response: LiveData<MyPageResponse>
        get() = _response

    var isTimeSet: Boolean = false
    var days: MutableSet<Day> = mutableSetOf()

    fun getData(onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            myPageRepository.getResponse().apply {
                this.onSuccess {
                    _response.value = it
                    isTimeSet = it.hasPushAlarm && it.trainingTime.isSet()
                    days.addAll(it.trainingTime.day)
                }
                this.onFailure { onError(it) }
            }
        }
    }

    fun isDaySelected(content: String) = days.contains(Day.from(content))
    fun addDay(content: String) = days.add(Day.from(content))
    fun removeDay(content: String) = days.remove(Day.from(content))
}