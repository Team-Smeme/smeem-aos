package com.sopt.smeem.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Anonymous
import com.sopt.smeem.SmeemException
import com.sopt.smeem.SocialType
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.LoginResult
import com.sopt.smeem.domain.repository.LocalRepository
import com.sopt.smeem.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginVM @Inject constructor(
    private val localRepository: LocalRepository,
    @Anonymous private val loginRepository: LoginRepository
) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult

    fun login(
        kakaoAccessToken: String,
        socialType: SocialType,
        onError: (SmeemException) -> Unit
    ) {
        viewModelScope.launch {
            loginRepository.execute(kakaoAccessToken, socialType)
                .onSuccess {
                    _loginResult.value = it
                }
                .onHttpFailure { e -> onError(e) }
        }
    }
}