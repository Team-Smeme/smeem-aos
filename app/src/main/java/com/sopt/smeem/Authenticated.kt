package com.sopt.smeem

import javax.inject.Qualifier

@Qualifier
annotation class Authenticated(
    val isApplied: Boolean = true
)
