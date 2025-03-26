package com.sopt.smeem.presentation.coach

sealed class CoachSideEffect {
    data object NavigateToCoachDetail : CoachSideEffect()
    data object NavigateToHome : CoachSideEffect()
    data class ShowError(val message: String) : CoachSideEffect()
}
