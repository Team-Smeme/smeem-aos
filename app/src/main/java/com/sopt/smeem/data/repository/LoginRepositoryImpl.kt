package com.sopt.smeem.data.repository

import com.sopt.smeem.data.model.request.LoginRequest
import com.sopt.smeem.data.service.LoginService
import com.sopt.smeem.domain.common.ApiResult
import com.sopt.smeem.domain.dto.LoginResultDto
import com.sopt.smeem.domain.model.SocialType
import com.sopt.smeem.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val loginService: LoginService,
) : LoginRepository {
    override suspend fun execute(
        accessToken: String,
        socialType: SocialType,
        fcmToken: String
    ): ApiResult<LoginResultDto> =
        loginService.login("Bearer $accessToken", LoginRequest(socialType, fcmToken))
            .let { response ->
                if (response.isSuccessful) {
                    response.body()!!.data.let { data ->
                        ApiResult(
                            response.code(),
                            LoginResultDto(
                                apiAccessToken = data.accessToken,
                                apiRefreshToken = data.refreshToken,
                                isRegistered = data.isRegistered,
                                isPlanRegistered = data.hasPlan,
                            )
                        )
                    }
                } else {
                    throw response.code().handleStatusCode()
                }

            }

    override suspend fun checkNicknameDuplicated(nickname: String): ApiResult<Boolean> =
        loginService.checkDuplicated(nickname).let { response ->
            if (response.isSuccessful) {
                ApiResult(response.code(), response.body()!!.data.isExist)
            } else {
                throw response.code().handleStatusCode()
            }
        }
}