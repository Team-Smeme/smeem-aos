package com.sopt.smeem.domain.model

data class Authentication(
    val accessToken: String,
    val refreshToken: String,
    var isAnonymous: Boolean = false,
)