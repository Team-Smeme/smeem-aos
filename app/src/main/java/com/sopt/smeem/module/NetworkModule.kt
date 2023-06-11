package com.sopt.smeem.module

import com.sopt.smeem.BuildConfig.API_SERVER_URL
import com.sopt.smeem.domain.repository.AuthRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule @Inject constructor(
    private val authRepository: AuthRepository
) {
    val apiServerRetrofitForAnonymous by lazy {
        Retrofit.Builder()
            .baseUrl(API_SERVER_URL)
            .client(
                OkHttpClient.Builder().apply {
                    connectTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(5, TimeUnit.SECONDS)
                    readTimeout(5, TimeUnit.SECONDS)
                }.build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 인증 후 api Retrofit
    val apiServerRetrofitForAuthentication by lazy {
        Retrofit.Builder()
            .baseUrl(API_SERVER_URL)
            .client(
                OkHttpClient.Builder().apply {
                    connectTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(5, TimeUnit.SECONDS)
                    readTimeout(5, TimeUnit.SECONDS)

                    CoroutineScope(Dispatchers.IO).launch {

                        if (authRepository.isAuthenticated()) {
                            addInterceptor(
                                RequestInterceptor(
                                    accessToken = "Bearer ${authRepository.getAuthentication()!!.accessToken}",
                                    refreshToken = "Bearer ${authRepository.getAuthentication()!!.refreshToken}"
                                )
                            )
                        }
                    }

                }.build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    class RequestInterceptor(
        val accessToken: String,
        val refreshToken: String
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
            chain.request().newBuilder().apply {
                header(API_ACCESS_TOKEN_HEADER, "Bearer $accessToken")
                header(
                    API_REFRESH_TOKEN_HEADER,
                    "Bearer $refreshToken"
                ) // TODO : 서버에서 정의되는 refresh token 정책에 맞춰 구성
            }.build()
        )

        private val API_ACCESS_TOKEN_HEADER = "Authorization"
        private val API_REFRESH_TOKEN_HEADER = "not yet" // TODO
    }
}