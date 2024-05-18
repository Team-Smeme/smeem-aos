package com.sopt.smeem.util

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsManager {
    private val crashlytics = FirebaseCrashlytics.getInstance()

    fun logException(t: Throwable) {
        crashlytics.recordException(t)
    }

    fun logMessage(message: String) {
        crashlytics.log(message)
    }
}