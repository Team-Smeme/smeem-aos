package com.sopt.smeem.presentation.coach.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface CoachRoute {
    @Serializable
    data object Coach : CoachRoute

    @Serializable
    data object CoachDetail : CoachRoute
}
