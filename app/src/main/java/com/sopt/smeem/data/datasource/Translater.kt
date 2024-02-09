package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.model.request.DeepLRequest
import com.sopt.smeem.data.model.response.DeepLApiResponse
import com.sopt.smeem.data.service.DeepLApiService
import retrofit2.Response

class Translater(
    private val deepLApiService: DeepLApiService,
) {
    suspend fun translateKo2En(text: String): Response<DeepLApiResponse> {
        return deepLApiService.translate(
            DeepLRequest(text = listOf(text), source_lang = "ko", target_lang = "en")
        )
    }
}