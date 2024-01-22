package com.sopt.smeem.module

import com.sopt.smeem.BuildConfig
import com.sopt.smeem.BuildConfig.DEV_API_SERVER_URL
import com.sopt.smeem.BuildConfig.PROD_API_SERVER_URL
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
    private val apiServer = if (BuildConfig.IS_DEBUG) DEV_API_SERVER_URL else PROD_API_SERVER_URL

    val apiServerRetrofitForAnonymous: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(apiServer)
            .client(
                OkHttpClient.Builder().apply {
                    connectTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(5, TimeUnit.SECONDS)
                    readTimeout(5, TimeUnit.SECONDS)

                    addInterceptor(VersionInterceptor())
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        },
                    )
                }
                    .build(),
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 인증 후 api Retrofit
    val apiServerRetrofitForAuthentication: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(apiServer)
            .client(
                OkHttpClient.Builder().apply {
                    connectTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(5, TimeUnit.SECONDS)
                    readTimeout(5, TimeUnit.SECONDS)

                    addInterceptor(VersionInterceptor())
                    runBlocking {
                        addInterceptor(
                            RequestInterceptor(
                                accessToken = localRepository.getAuthentication().accessToken,
                                refreshToken = localRepository.getAuthentication().refreshToken,
                            ),
                        )
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

    class VersionInterceptor() : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
            chain.request().newBuilder().apply {
                addHeader("app-version", BuildConfig.VERSION_NAME)
                addHeader("platform", "Android")
            }.build(),
        )
    }

    val apiDeepLAPIRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api-free.deepl.com/")
            .client(
                OkHttpClient.Builder().apply {
                    connectTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(5, TimeUnit.SECONDS)
                    readTimeout(5, TimeUnit.SECONDS)
                }.addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    },
                ).addInterceptor(DeepLInterceptor(BuildConfig.DEEPL_API_KEY))
                    .build(),
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    class DeepLInterceptor(
        val token: String,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
            chain.request().newBuilder().apply {
                addHeader(API_ACCESS_TOKEN_HEADER, "DeepL-Auth-Key $token")
            }.build(),
        )

        private val API_ACCESS_TOKEN_HEADER = "Authorization"
    }
}
