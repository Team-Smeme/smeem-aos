package com.sopt.smeem.presentation.auth.splash

import android.content.Context

interface OAuthHandler {
    fun isAppEnabled(context: Context): Boolean

    fun loginOnApp(
        context: Context,
        onSuccess: (String, String) -> Unit,
        onFailed: (Throwable) -> Unit
    )

    fun loginOnWeb(
        context: Context, onSuccess: (String, String) -> Unit,
        onFailed: (Throwable) -> Unit
    )
}