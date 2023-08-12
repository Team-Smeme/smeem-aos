package com.sopt.smeem.data.service

import com.sopt.smeem.data.model.response.NaverPapagoResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header

interface PapagoService {
    @GET("/v1/papago/n2mt")
    suspend fun translate(
        @Field("source") source: String,
        @Field("target") target: String,
        @Field("text") text: String,
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") secretKey: String,
    ): Call<NaverPapagoResponse>
}