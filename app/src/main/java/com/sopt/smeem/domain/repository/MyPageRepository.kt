package com.sopt.smeem.domain.repository

import com.sopt.smeem.data.model.response.MyPageResponse

interface MyPageRepository {
    suspend fun getResponse(): Result<MyPageResponse>
}