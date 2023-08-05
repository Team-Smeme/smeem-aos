package com.sopt.smeem.presentation.auth

import android.content.Context
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.ApiError
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.user.UserApiClient
import com.sopt.smeem.SmeemErrorCode
import com.sopt.smeem.SmeemException

object KakaoHandler : OAuthHandler {
    override fun isAppEnabled(context: Context) =
        UserApiClient.instance.isKakaoTalkLoginAvailable(context)

    override fun loginOnApp(
        context: Context,
        onSuccess: (String, String) -> Unit,
        onFailed: (SmeemException) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                handleError(error) { onFailed(it) }
            } else if (token != null) {
                if (AuthApiClient.instance.hasToken()) {
                    onSuccess(token.accessToken, token.refreshToken)
                }
            }
        }
    }

    override fun loginOnWeb(
        context: Context,
        onSuccess: (String, String) -> Unit,
        onFailed: (SmeemException) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                handleError(error) { onFailed(it) }
            } else if (token != null) {
                if (AuthApiClient.instance.hasToken()) {
                    onSuccess(token.accessToken, token.refreshToken)
                }
            }
        }
    }

    private fun handleError(error: Throwable, catch: (SmeemException) -> Unit) {
        try {
            when (error) {
                is ClientError -> { // Client 에서 발생되어지는 예외
                    throw SmeemException(
                        errorCode = SmeemErrorCode.CLIENT_ERROR,
                        logMessage = "카카오 로그인 중 client 에서 발생시킨 에러 ${error.reason}",
                        throwable = error
                    )
                }

                is AuthError -> { // 인증 과정 중 발생되어지는 예외
                    throw SmeemException(
                        errorCode = SmeemErrorCode.CLIENT_ERROR,
                        logMessage = "카카오 로그인 중 인증 과정에서 발생된 에러 (${error.reason})",
                        throwable = error
                    )
                }

                is ApiError -> { // Api Call 도중 발생되어지는 예외
                    throw SmeemException(
                        errorCode = SmeemErrorCode.CLIENT_ERROR,
                        logMessage = "KAKAO API 호출 중 발생된 에러 (${error.reason})",
                        throwable = error
                    )
                }

                else -> {
                    throw SmeemException(
                        errorCode = SmeemErrorCode.SYSTEM_ERROR,
                        logMessage = "카카오 로그인 중 비정상적으로 발생된 에러",
                        throwable = error
                    )
                }
            }
        } catch (e: SmeemException) {
            catch(e)
        }
    }
}