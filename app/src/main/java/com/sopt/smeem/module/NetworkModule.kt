package com.sopt.smeem.module

import com.sopt.smeem.BuildConfig.API_SERVER_URL
import com.sopt.smeem.domain.repository.LocalRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule @Inject constructor(
    private val localRepository: LocalRepository,
) {
    val apiServerRetrofitForAnonymous by lazy {
        Retrofit.Builder()
            .baseUrl(API_SERVER_URL)
            .client(
                OkHttpClient.Builder().apply {
                    connectTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(5, TimeUnit.SECONDS)
                    readTimeout(5, TimeUnit.SECONDS)
                }.addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    },
                )
                    .build(),
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

                    runBlocking { // TODO : 제거
                        if (localRepository.isAuthenticated()) {
                            addInterceptor(
                                RequestInterceptor(
                                    accessToken = localRepository.getAuthentication()!!.accessToken!!,
                                    refreshToken = localRepository.getAuthentication()!!.refreshToken,
                                ),
                            )
                        }
                    }
                }.addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    },
                ).build(),
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    class RequestInterceptor(
        val accessToken: String,
        val refreshToken: String?,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
            chain.request().newBuilder().apply {
                addHeader(API_ACCESS_TOKEN_HEADER, "Bearer $accessToken")
                addHeader(API_REFRESH_TOKEN_HEADER, "Bearer ${refreshToken ?: ""}")
            }.build(),
        )

        private val API_ACCESS_TOKEN_HEADER = "Authorization"
        private val API_REFRESH_TOKEN_HEADER = "Refresh" // TODO
    }
}
