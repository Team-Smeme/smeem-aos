package com.sopt.smeem.data

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(
    val accessToken: String,
    val refreshToken: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
        chain.request().newBuilder().apply {
            header(API_ACCESS_TOKEN_HEADER, "Bearer $accessToken")
            header(API_REFRESH_TOKEN_HEADER, "Bearer $refreshToken") // TODO : 서버에서 정의되는 refresh token 정책에 맞춰 구성
        }.build()
    )
}

internal val API_ACCESS_TOKEN_HEADER = "Authorization"
internal val API_REFRESH_TOKEN_HEADER = "not yet" // TODO