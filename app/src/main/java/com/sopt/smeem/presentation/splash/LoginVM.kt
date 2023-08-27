package com.sopt.smeem.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.sopt.smeem.Anonymous
import com.sopt.smeem.SmeemException
import com.sopt.smeem.SocialType
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Authentication
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
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                viewModelScope.launch {
                    loginRepository.execute(kakaoAccessToken, socialType, it.result)
                        .onSuccess {
                            _loginResult.value = it
                        }
                        .onHttpFailure { e -> onError(e) }
                }
            }
        }
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
}