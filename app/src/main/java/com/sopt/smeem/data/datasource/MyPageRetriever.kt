package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.ApiPool
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.MyPageResponse

class MyPageRetriever {
    suspend fun getResponse(): ApiResponse<MyPageResponse> = ApiPool.myPageService.getMyInfo()
}