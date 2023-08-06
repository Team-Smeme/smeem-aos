package com.sopt.smeem.module

import com.sopt.smeem.Authenticated
import com.sopt.smeem.data.datasource.LoginExecutor
import com.sopt.smeem.data.datasource.TrainingManager
import com.sopt.smeem.data.repository.LoginRepositoryImpl
import com.sopt.smeem.data.repository.TrainingRepositoryImpl
import com.sopt.smeem.data.repository.UserRepositoryImpl
import com.sopt.smeem.data.service.LoginService
import com.sopt.smeem.data.service.TrainingService
import com.sopt.smeem.data.service.UserService
import com.sopt.smeem.domain.repository.LoginRepository
import com.sopt.smeem.domain.repository.TrainingRepository
import com.sopt.smeem.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AnonymousModule {
    @Provides
    @ViewModelScoped
    @Authenticated(isApplied = false)
    fun anonymousMemberRepository(networkModule: NetworkModule): UserRepository =
        UserRepositoryImpl(
            TrainingManager(
                userService = networkModule.apiServerRetrofitForAnonymous.create(UserService::class.java),
                trainingService = networkModule.apiServerRetrofitForAnonymous.create(TrainingService::class.java)
            )
        )

    @Provides
    @ViewModelScoped
    @Authenticated(isApplied = false)
    fun loginRepository(networkModule: NetworkModule): LoginRepository =
        LoginRepositoryImpl(
            LoginExecutor(
                networkModule.apiServerRetrofitForAnonymous.create(
                    LoginService::class.java
                )
            )
        )

    @Provides
    @ViewModelScoped
    @Authenticated(isApplied = false)
    fun trainingRepository(
        networkModule: NetworkModule
    ): TrainingRepository {
        val trainingService = networkModule.apiServerRetrofitForAnonymous.create(
            TrainingService::class.java
        )

        return TrainingRepositoryImpl(TrainingManager(trainingService = trainingService))
    }
}