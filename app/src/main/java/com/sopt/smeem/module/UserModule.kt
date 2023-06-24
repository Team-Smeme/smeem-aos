package com.sopt.smeem.module

import com.sopt.smeem.Authenticated
import com.sopt.smeem.data.datasource.JoinHelper
import com.sopt.smeem.data.datasource.TrainingManager
import com.sopt.smeem.data.repository.UserRepositoryImpl
import com.sopt.smeem.data.service.UserService
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

        return UserRepositoryImpl(
            trainingManager = TrainingManager(userService),
            joinHelper = JoinHelper(userService)
        )
    }
}