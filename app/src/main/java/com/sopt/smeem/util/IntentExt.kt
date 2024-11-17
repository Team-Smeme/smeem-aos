package com.sopt.smeem.util

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Parcelable

fun <T : Parcelable?> Intent.getParcelable(
    key: String,
    clazz: Class<T>,
): T? =
    when {
        SDK_INT >= TIRAMISU -> this.getParcelableExtra(key, clazz)
        else -> this.getParcelableExtra(key) as? T
    }

inline fun <reified T : Parcelable> Intent.getParcelableArrayListExtraCompat(key: String): List<T>? =
    when {
        SDK_INT >= TIRAMISU -> {
            getParcelableArrayListExtra(key, T::class.java)
        }

        else -> {
            @Suppress("DEPRECATION")
            getParcelableArrayListExtra(key)
        }
    }
