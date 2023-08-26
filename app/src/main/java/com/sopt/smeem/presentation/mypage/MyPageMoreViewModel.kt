package com.sopt.smeem.presentation.mypage

import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.repository.LocalRepository
import com.sopt.smeem.domain.repository.UserRepository
import com.sopt.smeem.presentation.health.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
internal class MyPageMoreViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val userRepository: UserRepository
): ViewModel() {

    fun clearLocal() {
        runBlocking {
            localRepository.clear()
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            userRepository.deleteUser()
        }

        runBlocking {
            localRepository.clear()
        }
    }
}