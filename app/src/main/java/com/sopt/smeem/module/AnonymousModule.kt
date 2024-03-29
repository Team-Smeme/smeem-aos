package com.sopt.smeem.module

import com.sopt.smeem.Anonymous
import com.sopt.smeem.data.datasource.LoginExecutor
import com.sopt.smeem.data.datasource.MyBadgeRetriever
import com.sopt.smeem.data.datasource.MyPageRetriever
import com.sopt.smeem.data.datasource.TrainingManager
import com.sopt.smeem.data.datasource.UserModifier
import com.sopt.smeem.data.repository.LoginRepositoryImpl
import com.sopt.smeem.data.repository.TrainingRepositoryImpl
import com.sopt.smeem.data.repository.UserRepositoryImpl
import com.sopt.smeem.data.service.LoginService
import com.sopt.smeem.data.service.MyBadgeService
import com.sopt.smeem.data.service.MyPageService
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
    @Anonymous
    fun anonymousMemberRepository(networkModule: NetworkModule): UserRepository =
        UserRepositoryImpl(
            trainingManager = TrainingManager(
                userService = networkModule.apiServerRetrofitForAnonymous.create(UserService::class.java),
                trainingService = networkModule.apiServerRetrofitForAnonymous.create(TrainingService::class.java)
            ),
            userModifier = UserModifier(networkModule.apiServerRetrofitForAnonymous.create(UserService::class.java)),
            myPageRetriever = MyPageRetriever(networkModule.apiServerRetrofitForAnonymous.create(MyPageService::class.java)),
            myBadgeRetriever = MyBadgeRetriever(networkModule.apiServerRetrofitForAnonymous.create(MyBadgeService::class.java)),
        )

    @Provides
    @ViewModelScoped
    @Anonymous
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
    @Anonymous
    fun trainingRepository(
        networkModule: NetworkModule
    ): TrainingRepository {
        val trainingService = networkModule.apiServerRetrofitForAnonymous.create(
            TrainingService::class.java
        )

        return TrainingRepositoryImpl(TrainingManager(trainingService = trainingService))
    }
}