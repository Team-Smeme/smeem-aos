package com.sopt.smeem.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.model.Authentication
import com.sopt.smeem.domain.repository.LocalRepository
import com.sopt.smeem.domain.repository.LoginRepository
import com.sopt.smeem.domain.repository.VersionRepository
import com.sopt.smeem.module.Anonymous
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashVM @Inject constructor(
    private val localRepository: LocalRepository,
    @Anonymous private val loginRepository: LoginRepository,
    @Anonymous private val versionRepository: VersionRepository
) : ViewModel() {
    private val _isAuthed = MutableLiveData<Boolean>()
    val isAuthed: LiveData<Boolean> = _isAuthed

    private val _version = MutableStateFlow("")
    val version: StateFlow<String> = _version.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1500)

            getVersion { t ->
                Timber.e(t)
            }
            checkAuthed()
        }
    }

    private fun checkAuthed() {
        viewModelScope.launch {
            try {
                val authentication = localRepository.getAuthentication()

                // 처음으로 접속하는 유저
                if (authentication == null) {
                    _isAuthed.value = false
                    return@launch
                }

                val refreshToken = authentication.refreshToken
                val refreshResult = loginRepository.getToken("Bearer $refreshToken")

                if (refreshResult.statusCode == 200) {
                    val newAccessToken = refreshResult.data().accessToken
                    saveTokenOnLocal(newAccessToken, refreshToken)
                    _isAuthed.value = true
                } else {
                    _isAuthed.value = false
                }
            } catch (e: Exception) {
                _isAuthed.value = false
            }
        }
    }


    private fun getVersion(onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                versionRepository.getVersion().run {
                    _version.value = data().androidVersion.version
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    private fun saveTokenOnLocal(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            localRepository.setAuthentication(
                Authentication(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            )
        }
    }
}