package com.sopt.smeem.data.repository

import com.sopt.smeem.SocialType
import com.sopt.smeem.data.datasource.LoginExecutor
import com.sopt.smeem.domain.model.LoginResult
import com.sopt.smeem.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val loginExecutor: LoginExecutor
) : LoginRepository {
    override suspend fun execute(accessToken: String, socialType: SocialType): Result<LoginResult> =
        kotlin.runCatching { loginExecutor.execute(accessToken, socialType) }.map { response ->
            LoginResult.from(response.data!!)
        }

    override suspend fun checkNicknameDuplicated(nickname: String): Result<Boolean> =
        kotlin.runCatching { loginExecutor.checkNicknameDuplicated(nickname) }
            .map { response -> response.data!!.isExist }

}