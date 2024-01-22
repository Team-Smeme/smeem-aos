package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.request.DeepLRequest
import com.sopt.smeem.data.model.response.DeepLApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.POST

interface DeepLApiService {
    @POST("/v2/translate")
    suspend fun translate(
        @Body request: DeepLRequest,
    ): Response<DeepLApiResponse>
}