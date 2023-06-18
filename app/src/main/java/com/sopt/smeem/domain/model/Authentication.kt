package com.sopt.smeem.domain.model

data class Authentication(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    var isAnonymous: Boolean = accessToken == null,
) {
    fun isAuthenticated() = accessToken != null
}