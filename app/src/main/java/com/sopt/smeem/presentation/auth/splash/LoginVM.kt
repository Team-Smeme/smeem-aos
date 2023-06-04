package com.sopt.smeem.presentation.auth.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.SmeemException
import com.sopt.smeem.SocialType
import com.sopt.smeem.data.datasource.Login
import com.sopt.smeem.data.onHttpFailure
import com.sopt.smeem.data.repository.LoginRepositoryImpl
import com.sopt.smeem.domain.model.auth.LoginResult
import com.sopt.smeem.domain.repository.LoginRepository
import kotlinx.coroutines.launch

// TODO : 이후 sociak 로그인 많아지면 social 별 VM 분리

internal class LoginVM(
    private val loginRepository: LoginRepository = LoginRepositoryImpl(Login())
) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult

    fun login(
        idToken: String,
        socialType: SocialType,
        onError: (SmeemException) -> Unit
    ) {
        viewModelScope.launch {
            loginRepository.execute(idToken, socialType)
                .onSuccess { _loginResult.value = it }
                .onHttpFailure { e -> onError(e) }
        }
    }
}