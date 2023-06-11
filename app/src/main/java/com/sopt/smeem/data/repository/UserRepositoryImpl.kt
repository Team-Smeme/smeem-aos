package com.sopt.smeem.data.repository

import com.sopt.smeem.data.datasource.PlanSetter
import com.sopt.smeem.domain.model.auth.OnBoarding
import com.sopt.smeem.domain.repository.UserRepository

class UserRepositoryImpl(
    private val planSetter: PlanSetter
) : UserRepository {
    override suspend fun patchOnBoarding(onBoarding: OnBoarding): Result<Unit> =
        kotlin.runCatching { planSetter.patch(onBoarding) }
}