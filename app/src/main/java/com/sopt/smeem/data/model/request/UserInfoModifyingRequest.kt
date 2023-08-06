package com.sopt.smeem.data.model.request

data class UserInfoModifyingRequest(
    val username: String,
    val termAccepted: Boolean,
)