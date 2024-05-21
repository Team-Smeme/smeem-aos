package com.sopt.smeem

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.appsflyer.AppsFlyerLib
import com.appsflyer.deeplink.DeepLinkResult
import com.kakao.sdk.common.KakaoSdk
import com.sopt.smeem.BuildConfig.APPFLYER_DEV_KEY
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
        setOnelink()
    }

    private fun setOnelink() {
        val appsFlyer = AppsFlyerLib.getInstance()
        appsFlyer.init(APPFLYER_DEV_KEY, null, this)
        appsFlyer.subscribeForDeepLink { deepLinkResult ->
            when (deepLinkResult.status) {
                DeepLinkResult.Status.FOUND -> {
                    Timber.e("DeepLinking: ${deepLinkResult.deepLink}")
                }

                DeepLinkResult.Status.NOT_FOUND -> {
                    Timber.e("DeepLinking: NOT_FOUND")
                }

                DeepLinkResult.Status.ERROR -> {
                    val error = deepLinkResult.error
                    Timber.e("DeepLinking: ERROR $error")
                }
            }
            val deepLink = deepLinkResult.deepLink
            try {
                Timber.e("DeepLinking: $deepLink")
            } catch (e: Exception) {
                Timber.e("DeepLinking: $e")

            }
        }
    }

    companion object {
        private lateinit var _amplitude: Amplitude
        val AMPLITUDE: Amplitude
            get() = _amplitude
    }
}
