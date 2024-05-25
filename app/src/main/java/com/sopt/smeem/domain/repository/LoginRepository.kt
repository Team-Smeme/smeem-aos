package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.common.ApiResult
import com.sopt.smeem.domain.dto.LoginResultDto
import com.sopt.smeem.domain.model.SocialType

interface LoginRepository {
    suspend fun execute(
        accessToken: String,
        socialType: SocialType,
        fcmToken: String
    ): ApiResult<LoginResultDto>

    suspend fun checkNicknameDuplicated(nickname: String): ApiResult<Boolean>
}