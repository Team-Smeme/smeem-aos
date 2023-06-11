package com.sopt.smeem.module

import com.sopt.smeem.Authenticated
import com.sopt.smeem.data.datasource.PlanSetter
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
object AnonymousModule {
    @Provides
    @ViewModelScoped
    @Authenticated(isApplied = false)
    fun anonymousMemberRepository(
        networkModule: NetworkModule
    ): UserRepository =
        UserRepositoryImpl(PlanSetter(networkModule.apiServerRetrofitForAnonymous.create(UserService::class.java)))
}