package com.sopt.smeem.util

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.Serializable

fun <T : Serializable?> Intent.getSerializable(
    key: String,
    clazz: Class<T>,
): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this.getSerializableExtra(key, clazz)
        else -> this.getSerializableExtra(key) as? T
    }

fun <T : Parcelable?> Intent.getParcelable(
    key: String,
    clazz: Class<T>,
): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this.getParcelableExtra(key, clazz)
        else -> this.getParcelableExtra(key) as? T
    }