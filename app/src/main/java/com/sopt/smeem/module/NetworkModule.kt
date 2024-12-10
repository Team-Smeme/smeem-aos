package com.sopt.smeem.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sopt.smeem.BuildConfig
import com.sopt.smeem.BuildConfig.DEV_API_SERVER_URL
import com.sopt.smeem.BuildConfig.PROD_API_SERVER_URL
import com.sopt.smeem.domain.repository.LocalRepository
import dagger.Module
import dagger.Provides
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val apiServer = if (BuildConfig.IS_DEBUG) DEV_API_SERVER_URL else PROD_API_SERVER_URL
    private val gson: Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    @AnonymousRetrofit
    fun provideApiServerRetrofitForAnonymous(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiServer)
            .client(
                OkHttpClient.Builder().apply {
                    connectTimeout(15, TimeUnit.SECONDS)
                    writeTimeout(30, TimeUnit.SECONDS)
                    readTimeout(30, TimeUnit.SECONDS)
                    addInterceptor(VersionInterceptor())
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        },
                    )
                }.build(),
            )
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @AuthenticationRetrofit
    fun provideApiServerRetrofitForAuthentication(
        localRepository: LocalRepository
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(apiServer)
            .client(
                OkHttpClient.Builder().apply {
                    connectTimeout(15, TimeUnit.SECONDS)
                    writeTimeout(30, TimeUnit.SECONDS)
                    readTimeout(30, TimeUnit.SECONDS)

                    addInterceptor(VersionInterceptor())
                    runBlocking {
                        val authentication = requireNotNull(localRepository.getAuthentication()) {
                            "Authentication information is missing. Cannot create authenticated Retrofit client."
                        }
                        addInterceptor { chain ->
                            val request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer ${authentication.accessToken}")
                                .build()
                            chain.proceed(request)
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

    class VersionInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
            chain.request().newBuilder().apply {
                addHeader("app-version", BuildConfig.VERSION_NAME)
                addHeader("platform", "Android")
            }.build(),
        )
    }

    @Provides
    @Singleton
    @DeepLRetrofit
    fun provideDeepLRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api-free.deepl.com/")
            .client(
                OkHttpClient.Builder().apply {
                    connectTimeout(10, TimeUnit.SECONDS)
                    writeTimeout(30, TimeUnit.SECONDS)
                    readTimeout(30, TimeUnit.SECONDS)
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
        private val token: String,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
            chain.request().newBuilder().apply {
                addHeader(API_ACCESS_TOKEN_HEADER, "DeepL-Auth-Key $token")
            }.build(),
        )

        private val API_ACCESS_TOKEN_HEADER = "Authorization"
    }
}
