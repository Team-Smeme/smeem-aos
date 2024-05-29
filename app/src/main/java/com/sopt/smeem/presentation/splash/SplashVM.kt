package com.sopt.smeem.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.repository.LocalRepository
import com.sopt.smeem.domain.repository.VersionRepository
import com.sopt.smeem.module.Anonymous
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashVM @Inject constructor(
    private val localRepository: LocalRepository,
    @Anonymous private val versionRepository: VersionRepository
) : ViewModel() {
    private val _isAuthed = MutableLiveData<Boolean>()
    val isAuthed: LiveData<Boolean>
        get() = _isAuthed

    private val _version = MutableStateFlow("")
    val version = _version.asStateFlow()

    init {
        getVersion { t ->
            Timber.e(t)
        }
    }

    fun checkAuthed() {
        viewModelScope.launch {
            delay(1500L) // non-blocking
            _isAuthed.value = localRepository.isAuthenticated()
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
}