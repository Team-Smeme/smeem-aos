package com.sopt.smeem.data.model.request

import com.sopt.smeem.SocialType

data class LoginRequest(
    val social: SocialType,
    val fcmToken: String,
)