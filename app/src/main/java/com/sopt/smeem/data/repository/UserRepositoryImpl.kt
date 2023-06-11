package com.sopt.smeem.data.repository

import com.sopt.smeem.data.datasource.JoinHelper
import com.sopt.smeem.data.datasource.PlanSetter
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.auth.Joining
import com.sopt.smeem.domain.repository.UserRepository

class UserRepositoryImpl(
    private val planSetter: PlanSetter? = null,
    private val joinHelper: JoinHelper? = null
) : UserRepository {
    override suspend fun patchOnBoarding(onBoarding: OnBoarding): Result<Unit> =
        kotlin.runCatching { planSetter!!.patch(onBoarding) }

    override suspend fun patchNicknameAndAcceptance(
        username: String,
        marketingAcceptance: Boolean?
    ): Result<Joining> =
        kotlin.runCatching {
            joinHelper!!.patch(
                username = username,
                marketingAcceptance = marketingAcceptance
            )
        }.map { response ->
            Joining(
                username = response.data!!.username,
                marketingAcceptance = response.data.ternAccepted
            )
        }
}