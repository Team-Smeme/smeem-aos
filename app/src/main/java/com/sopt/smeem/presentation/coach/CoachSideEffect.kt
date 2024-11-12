package com.sopt.smeem.presentation.coach

sealed class CoachSideEffect {
    data object NavigateToCoachDetail : CoachSideEffect()
}
