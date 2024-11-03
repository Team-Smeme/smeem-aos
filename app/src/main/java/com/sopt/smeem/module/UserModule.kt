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
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object UserModule {
    @Provides
    @ViewModelScoped
    fun userRepository(
        @AuthenticationRetrofit apiServerRetrofitForAuthentication: Retrofit
    ): UserRepository {
        return UserRepositoryImpl(
            userService = apiServerRetrofitForAuthentication.create(
                UserService::class.java
            ),
            myBadgeService = apiServerRetrofitForAuthentication.create(
                MyBadgeService::class.java
            )
        )
    }

    @Provides
    @ViewModelScoped
    fun diaryRepository(
        @AuthenticationRetrofit apiServerRetrofitForAuthentication: Retrofit
    ): DiaryRepository {
        return DiaryRepositoryImpl(
            apiServerRetrofitForAuthentication.create(DiaryService::class.java),
            diaryReader = DiaryReader(
                apiServerRetrofitForAuthentication.create(DiaryService::class.java)
            )
        )
    }
}