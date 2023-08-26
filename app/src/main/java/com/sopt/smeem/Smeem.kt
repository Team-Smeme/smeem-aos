package com.sopt.smeem

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.sopt.smeem.BuildConfig.KAKAO_API_KEY
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Smeem : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        KakaoSdk.init(this, KAKAO_API_KEY)
    }
}
