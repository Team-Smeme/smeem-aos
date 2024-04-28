package com.sopt.smeem.module

import com.sopt.smeem.data.datasource.DiaryReader
import com.sopt.smeem.data.repository.DiaryRepositoryImpl
import com.sopt.smeem.data.repository.UserRepositoryImpl
import com.sopt.smeem.data.service.DiaryService
import com.sopt.smeem.data.service.MyBadgeService
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
    fun userRepository(networkModule: NetworkModule): UserRepository {
        return UserRepositoryImpl(
            userService = networkModule.apiServerRetrofitForAuthentication.create(
                UserService::class.java
            ),
            myBadgeService = networkModule.apiServerRetrofitForAuthentication.create(
                MyBadgeService::class.java
            )
        )
    }

    @Provides
    @ViewModelScoped
    fun diaryRepository(networkModule: NetworkModule): DiaryRepository {
        return DiaryRepositoryImpl(
            networkModule.apiServerRetrofitForAuthentication.create(DiaryService::class.java),
            diaryReader = DiaryReader(
                networkModule.apiServerRetrofitForAuthentication.create(DiaryService::class.java)
            )
        )
    }
}