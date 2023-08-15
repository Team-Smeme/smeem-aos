package com.sopt.smeem.data.datasource

import com.sopt.smeem.BuildConfig.NAVER_CLIENT_ID
import com.sopt.smeem.BuildConfig.NAVER_SECRET_KEY
import com.sopt.smeem.data.model.response.NaverPapagoResponse
import com.sopt.smeem.data.service.PapagoService
import retrofit2.Call
import retrofit2.Response

class Translater(
    private val papagoService: PapagoService
) {
    suspend fun translateKo2En(text: String): Response<NaverPapagoResponse> {
        return papagoService.translate(
            clientId = NAVER_CLIENT_ID,
            secretKey = NAVER_SECRET_KEY,
            source = "ko",
            target = "en",
            text = text
        )
    }
}