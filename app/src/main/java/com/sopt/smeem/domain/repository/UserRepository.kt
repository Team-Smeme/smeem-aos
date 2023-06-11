package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.auth.OnBoarding

interface UserRepository {
    suspend fun patchOnBoarding(onBoarding: OnBoarding): Result<Unit>
}