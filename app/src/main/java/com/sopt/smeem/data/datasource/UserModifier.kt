package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.model.request.UserInfoModifyingRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.service.UserService

class UserModifier(
    private val userService: UserService
) {
    suspend fun patch(
        username: String,
        marketingAcceptance: Boolean?
    ): ApiResponse<Unit> = userService.patchUserInfo(
        UserInfoModifyingRequest(username = username, termAccepted = marketingAcceptance ?: false)
    )
}