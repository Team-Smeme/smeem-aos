package com.sopt.smeem.presentation.auth

interface LoginProcess {
    suspend fun saveTokensInStore(accessToken: String?, refreshToken: String?) {
        // TODO : accessToken 이 null 일 경우 처리 무시
    }
}

