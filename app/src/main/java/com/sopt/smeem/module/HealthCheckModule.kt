package com.sopt.smeem.module

import com.sopt.smeem.data.repository.HealthRepositoryImpl
import com.sopt.smeem.data.service.HealthService
import com.sopt.smeem.domain.repository.HealthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object HealthCheckModule {
    @Provides
    @ViewModelScoped
    fun healthRepository(
        @AnonymousRetrofit apiServerRetrofitForAnonymous: Retrofit
    ): HealthRepository = HealthRepositoryImpl(
        apiServerRetrofitForAnonymous.create(HealthService::class.java)
    )
}