package com.sopt.smeem

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.kakao.sdk.common.KakaoSdk
import com.sopt.smeem.BuildConfig.DEV_AMPLITUDE_API_KEY
import com.sopt.smeem.BuildConfig.KAKAO_API_KEY
import com.sopt.smeem.BuildConfig.PROD_AMPLITUDE_API_KEY
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Smeem : Application() {
    override fun onCreate() {
        _amplitude = Amplitude(
            Configuration(
                apiKey = if (BuildConfig.DEBUG) DEV_AMPLITUDE_API_KEY else PROD_AMPLITUDE_API_KEY,
                context = this
            )
        )
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        KakaoSdk.init(this, KAKAO_API_KEY)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    companion object {
        private lateinit var _amplitude: Amplitude
        val AMPLITUDE: Amplitude
            get() = _amplitude
    }
}
