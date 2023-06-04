package com.sopt.smeem.data.model.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val isRegistered: Boolean,
    val hasPlan: Boolean
)
