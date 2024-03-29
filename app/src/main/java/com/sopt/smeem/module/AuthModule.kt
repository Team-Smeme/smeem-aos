package com.sopt.smeem.module

import android.content.Context
import com.sopt.smeem.data.repository.LocalRepositoryImpl
import com.sopt.smeem.domain.repository.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun authRepository(
        @ApplicationContext context: Context
    ): LocalRepository = LocalRepositoryImpl(context)
}