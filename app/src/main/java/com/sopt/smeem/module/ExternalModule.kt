package com.sopt.smeem.module

import com.sopt.smeem.Anonymous
import com.sopt.smeem.data.datasource.Translater
import com.sopt.smeem.data.repository.TranslateRepositoryImpl
import com.sopt.smeem.data.service.DeepLApiService
import com.sopt.smeem.data.service.PapagoService
import com.sopt.smeem.domain.repository.TranslateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ExternalModule {
    @Provides
    @ViewModelScoped
    @Anonymous
    fun translateRepository(networkModule: NetworkModule): TranslateRepository =
        TranslateRepositoryImpl(
            Translater(
                papagoService = networkModule.apiPapagoRetrofit.create(PapagoService::class.java),
                deepLApiService = networkModule.apiDeepLAPIRetrofit.create(DeepLApiService::class.java),
            )
        )
}