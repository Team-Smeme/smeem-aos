package com.sopt.smeem.module

import com.sopt.smeem.Authenticated
import com.sopt.smeem.data.datasource.LoginExecutor
import com.sopt.smeem.data.repository.LoginRepositoryImpl
import com.sopt.smeem.data.service.LoginService
import com.sopt.smeem.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object LoginModule {
    @Provides
    @ViewModelScoped
    @Authenticated(false)
    fun loginRepository(
        networkModule: NetworkModule
    ): LoginRepository =
        LoginRepositoryImpl(
            LoginExecutor(
                networkModule.apiServerRetrofitForAnonymous.create(
                    LoginService::class.java
                )
            )
        )
}