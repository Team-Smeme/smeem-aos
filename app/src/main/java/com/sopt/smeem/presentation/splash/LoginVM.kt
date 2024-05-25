package com.sopt.smeem.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.sopt.smeem.domain.dto.LoginResultDto
import com.sopt.smeem.domain.model.Authentication
import com.sopt.smeem.domain.model.SocialType
import com.sopt.smeem.domain.repository.LocalRepository
import com.sopt.smeem.domain.repository.LoginRepository
import com.sopt.smeem.module.Anonymous
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginVM @Inject constructor(
    private val localRepository: LocalRepository,
    @Anonymous private val loginRepository: LoginRepository
) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResultDto>()
    val loginResult: LiveData<LoginResultDto>
        get() = _loginResult

    fun login(
        kakaoAccessToken: String,
        socialType: SocialType,
        onError: (Throwable) -> Unit
    ) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { fcmTask ->
            if (fcmTask.isSuccessful) {
                viewModelScope.launch {
                    try {
                        loginRepository.execute(kakaoAccessToken, socialType, fcmTask.result).run {
                            _loginResult.value = data()
                        }
                    } catch (t: Throwable) {
                        onError(t)
                    }
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