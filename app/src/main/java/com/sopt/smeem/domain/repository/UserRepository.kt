package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.OnBoarding

interface UserRepository {
    suspend fun patchOnBoarding(onBoarding: OnBoarding): Result<Unit>
}