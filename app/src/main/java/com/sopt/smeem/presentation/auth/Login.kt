package com.sopt.smeem.presentation.auth

import android.content.Context
import android.widget.Toast

interface LoginProcess {
    fun sendServer(context: Context, socialToken: String): LoginResult {
        // coroutine launch //
        // TODO : 소셜 로그인 API call, token 을 header 에 담아서
        Toast.makeText(context, "소셜 로그인 API call", Toast.LENGTH_SHORT).show()

        // TODO : saveTokensInStore()

        return LoginResult(false, false)
    }

    fun doAfterLoginSuccess()
    suspend fun saveTokensInStore(accessToken: String?, refreshToken: String?) {
        // TODO : accessToken 이 null 일 경우 처리 무시
    }
}

data class LoginResult(
    val isRegistered: Boolean,
    val isPlanRegistered: Boolean
) {
    companion object {
        // TODO
    }
}