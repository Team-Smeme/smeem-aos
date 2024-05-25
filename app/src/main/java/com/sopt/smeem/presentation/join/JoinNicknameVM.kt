package com.sopt.smeem.presentation.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.repository.LoginRepository
import com.sopt.smeem.module.Anonymous
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
            try {
                loginRepository.checkNicknameDuplicated(content).run {
                    _nicknameDuplicated.value = data()
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}