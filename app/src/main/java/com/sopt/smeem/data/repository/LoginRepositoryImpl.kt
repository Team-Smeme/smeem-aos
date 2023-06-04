package com.sopt.smeem.data.repository

import com.sopt.smeem.SocialType
import com.sopt.smeem.data.datasource.Login
import com.sopt.smeem.domain.model.auth.LoginResult
import com.sopt.smeem.domain.repository.LoginRepository

class LoginRepositoryImpl(
    val login: Login
) : LoginRepository {
    override suspend fun execute(accessToken: String, socialType: SocialType): Result<LoginResult> =
        kotlin.runCatching { login.execute(accessToken, socialType) }.map { response ->
            LoginResult.from(response.data!!)
        }
}