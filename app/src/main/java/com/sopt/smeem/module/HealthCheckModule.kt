package com.sopt.smeem.module

import com.sopt.smeem.data.datasource.HealthChecker
import com.sopt.smeem.data.repository.HealthRepositoryImpl
import com.sopt.smeem.data.service.HealthService
import com.sopt.smeem.domain.repository.HealthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object HealthCheckModule {
    @Provides
    @ViewModelScoped
    fun healthRepository(
        networkModule: NetworkModule
    ): HealthRepository = HealthRepositoryImpl(
        HealthChecker(
            networkModule.apiServerRetrofitForAnonymous.create(HealthService::class.java)
        )
    )
}