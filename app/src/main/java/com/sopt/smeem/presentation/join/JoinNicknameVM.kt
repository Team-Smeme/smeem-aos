package com.sopt.smeem.presentation.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Anonymous
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class JoinNicknameVM @Inject constructor(
    @Anonymous private val loginRepository: LoginRepository,
) : ViewModel() {
    var content: String = ""

    private val _nicknameDuplicated = MutableLiveData<Boolean>()
    val nicknameDuplicated: LiveData<Boolean>
        get() = _nicknameDuplicated

    fun callApiNicknameDuplicated(onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            loginRepository.checkNicknameDuplicated(content)
                .onSuccess { result ->
                    _nicknameDuplicated.value = result
                }
                .onHttpFailure { onError(it) }
        }
    }
}