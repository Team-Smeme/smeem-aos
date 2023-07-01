package com.sopt.smeem.module

import com.sopt.smeem.Authenticated
import com.sopt.smeem.data.datasource.DiaryCommander
import com.sopt.smeem.data.datasource.DiaryReader
import com.sopt.smeem.data.datasource.JoinHelper
import com.sopt.smeem.data.datasource.TrainingManager
import com.sopt.smeem.data.repository.DiaryRepositoryImpl
import com.sopt.smeem.data.repository.UserRepositoryImpl
import com.sopt.smeem.data.service.DiaryService
import com.sopt.smeem.data.service.TrainingService
import com.sopt.smeem.data.service.UserService
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UserModule {
    @Provides
    @ViewModelScoped
    @Authenticated
    fun userRepository(
        networkModule: NetworkModule
    ): UserRepository {
        val userService = networkModule.apiServerRetrofitForAuthentication.create(
            UserService::class.java
        )
        val trainingService = networkModule.apiServerRetrofitForAuthentication.create(
            TrainingService::class.java
        )

        return UserRepositoryImpl(
            trainingManager = TrainingManager(userService, trainingService),
            joinHelper = JoinHelper(userService)
        )
    }

    @Provides
    @ViewModelScoped
    @Authenticated
    fun diaryRepository(
        networkModule: NetworkModule
    ): DiaryRepository {
        val diaryService = networkModule.apiServerRetrofitForAuthentication.create(
            DiaryService::class.java
        )

        return DiaryRepositoryImpl(
            diaryCommander = DiaryCommander(diaryService),
            diaryReader = DiaryReader(diaryService)
        )
    }
}