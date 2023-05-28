package com.sopt.smeem.presentation.auth.splash

import android.content.Context
import com.kakao.sdk.user.UserApiClient

object KakaoHandler : OAuthHandler {
    override fun isAppEnabled(context: Context) =
        UserApiClient.instance.isKakaoTalkLoginAvailable(context)

    override fun loginOnApp(
        context: Context,
        onSuccess: (String, String) -> Unit,
        onFailed: (Throwable) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                onFailed(error)
            } else if (token != null) {
                onSuccess(token.accessToken, token.refreshToken)
            }
        }
    }


    override fun loginOnWeb(
        context: Context,
        onSuccess: (String, String) -> Unit,
        onFailed: (Throwable) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = { token, error ->
            if (error != null) {
                onFailed(error)
            } else if (token != null) {
                onSuccess(token.accessToken, token.refreshToken)
            }
        })
    }
}