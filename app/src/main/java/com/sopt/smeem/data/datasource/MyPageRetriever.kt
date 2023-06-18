package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.MyPageResponse
import com.sopt.smeem.data.service.MyPageService

class MyPageRetriever(
    private val myPageService: MyPageService
) {
    suspend fun getResponse(): ApiResponse<MyPageResponse> = myPageService.getMyInfo()
}