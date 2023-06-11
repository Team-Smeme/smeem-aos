package com.sopt.smeem.domain.repository

import com.sopt.smeem.SocialType
import com.sopt.smeem.domain.model.LoginResult

interface LoginRepository {
    suspend fun execute(accessToken: String, socialType: SocialType): Result<LoginResult>
}