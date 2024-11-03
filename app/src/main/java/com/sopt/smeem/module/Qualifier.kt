package com.sopt.smeem.module

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AnonymousRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthenticationRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DeepLRetrofit

@Qualifier
annotation class Anonymous
