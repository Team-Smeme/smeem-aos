package com.sopt.smeem.data.repository

import com.sopt.smeem.data.datasource.MyPageRetriever
import com.sopt.smeem.data.model.response.MyPageResponse
import com.sopt.smeem.domain.repository.MyPageRepository

class MyPageRepositoryImpl(val myPageRetriever: MyPageRetriever) : MyPageRepository {
    override suspend fun getResponse(): Result<MyPageResponse> =
        kotlin.runCatching { myPageRetriever.getResponse() }.map { response -> response.data!! }
}