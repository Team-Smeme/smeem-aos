package com.sopt.smeem.module

import com.sopt.smeem.data.datasource.TrainingManager
import com.sopt.smeem.data.repository.TrainingRepositoryImpl
import com.sopt.smeem.data.service.TrainingService
import com.sopt.smeem.domain.repository.TrainingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrainingModule {
    @Provides
    @ViewModelScoped
    fun trainingRepository(
        networkModule: NetworkModule
    ): TrainingRepository {
        val trainingService = networkModule.apiServerRetrofitForAnonymous.create(
            TrainingService::class.java
        )

        return TrainingRepositoryImpl(TrainingManager(trainingService = trainingService))
    }
}