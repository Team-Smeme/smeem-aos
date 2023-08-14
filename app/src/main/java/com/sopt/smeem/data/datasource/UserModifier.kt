package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.model.request.UserInfoModifyingRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.service.UserService

class UserModifier(
    private val userService: UserService
) {
    suspend fun patch(
        accessToken: String?,
        username: String,
        marketingAcceptance: Boolean?
    ): ApiResponse<Unit> {
        if (accessToken != null) {
            return userService.patchUserInfoWithTokenFixed(
                token = "Bearer $accessToken",
                UserInfoModifyingRequest(
                    username = username,
                    termAccepted = marketingAcceptance ?: false
                )
            )
        } else {
            return userService.patchUserInfo(
                UserInfoModifyingRequest(
                    username = username,
                    termAccepted = marketingAcceptance ?: false
                )
            )
        }
    }
}