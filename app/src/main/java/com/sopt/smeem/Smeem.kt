package com.sopt.smeem

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.sopt.smeem.BuildConfig.KAKAO_API_KEY

class Smeem : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, KAKAO_API_KEY)
    }
}