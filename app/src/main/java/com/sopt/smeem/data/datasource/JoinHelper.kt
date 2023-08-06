package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.model.request.JoinRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.service.UserService

class JoinHelper(
    private val userService: UserService
) {
    suspend fun patch(
        username: String,
        marketingAcceptance: Boolean?
    ): ApiResponse<Unit> = userService.join(
        JoinRequest(username = username, termAccepted = marketingAcceptance ?: false)
    )
}