package com.sopt.smeem.presentation.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Anonymous
import com.sopt.smeem.domain.repository.LoginRepository
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ChangeNickNameViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @Anonymous private val loginRepository: LoginRepository
) : ContainerHost<ChangeNicknameUiState, ChangeNicknameSideEffect>, ViewModel() {

    override val container =
        container<ChangeNicknameUiState, ChangeNicknameSideEffect>(ChangeNicknameUiState())

    fun checkNicknameDuplicated(nickname: String) {
        if (nickname.isNotBlank()) {
            intent {
                reduce {
                    state.copy(nickname = nickname)
                }
            }

            viewModelScope.launch {
                try {
                    loginRepository.checkNicknameDuplicated(nickname)
                        .run { intent { reduce { state.copy(isDuplicated = data()) } } }
                } catch (t: Throwable) {
                    intent {
                        postSideEffect(ChangeNicknameSideEffect.ShowErrorToast(t.message ?: ""))
                    }
                }
            }
        }
    }

    fun changeNickname(nickname: String) {
        viewModelScope.launch {
            try {
                userRepository.modifyUsername(nickname)
                // 키보드가 완전히 내려지기 전까지 기다리는 시간 TODO: 상태 감지
                delay(800)
                intent {
                    postSideEffect(ChangeNicknameSideEffect.NavigateToSettingScreen)
                }
            } catch (t: Throwable) {
                intent {
                    postSideEffect(ChangeNicknameSideEffect.ShowErrorToast(t.message ?: ""))
                }
            }
        }
    }

}


data class ChangeNicknameUiState(
    val nickname: String = "",
    val isDuplicated: Boolean = false
)

sealed class ChangeNicknameSideEffect {
    data class ShowErrorToast(val message: String) : ChangeNicknameSideEffect()
    data object NavigateToSettingScreen : ChangeNicknameSideEffect()
}
