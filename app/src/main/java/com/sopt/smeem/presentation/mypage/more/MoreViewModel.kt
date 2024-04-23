package com.sopt.smeem.presentation.mypage.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.sopt.smeem.domain.repository.LocalRepository
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    fun clearLocal() {
        viewModelScope.launch {
            localRepository.clear()
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            userRepository.deleteUser()
            UserApiClient.instance.unlink {}
            localRepository.clear()
        }
    }
}