package com.sopt.smeem.data.datasource

import com.sopt.smeem.BuildConfig.DEEPL_API_KEY
import com.sopt.smeem.data.model.response.DeepLApiResponse
import com.sopt.smeem.data.service.DeepLApiService
import retrofit2.Response

class Translater(
    private val deepLApiService: DeepLApiService,
) {
    suspend fun translateKo2En(text: String): Response<DeepLApiResponse> {
        return deepLApiService.translate(
            source = "ko",
            target = "en",
            text = text,
            apiKey = DEEPL_API_KEY
        )
    }
}