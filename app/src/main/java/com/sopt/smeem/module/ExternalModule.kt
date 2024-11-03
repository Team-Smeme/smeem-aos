package com.sopt.smeem.module

import com.sopt.smeem.data.datasource.Translater
import com.sopt.smeem.data.repository.TranslateRepositoryImpl
import com.sopt.smeem.data.service.DeepLApiService
import com.sopt.smeem.domain.repository.TranslateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object ExternalModule {
    @Provides
    @ViewModelScoped
    @Anonymous
    fun translateRepository(
        @DeepLRetrofit apiDeepLRetrofit: Retrofit
    ): TranslateRepository =
        TranslateRepositoryImpl(
            Translater(
                deepLApiService = apiDeepLRetrofit.create(DeepLApiService::class.java),
            )
        )
}