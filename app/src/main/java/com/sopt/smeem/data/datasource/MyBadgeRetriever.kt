package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.MyBadgesResponse
import com.sopt.smeem.data.service.MyBadgeService

class MyBadgeRetriever(
    private val myBadgeService: MyBadgeService
) {
    suspend fun getResponse() : ApiResponse<MyBadgesResponse> = myBadgeService.getBadges()
}