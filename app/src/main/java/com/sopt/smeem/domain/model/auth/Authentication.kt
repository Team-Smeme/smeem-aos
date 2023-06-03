package com.sopt.smeem.domain.model.auth

data class Authentication(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val isAnonymous: Boolean = accessToken == null,
)