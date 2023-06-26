package com.sopt.smeem.util

import android.content.Context

fun Int.dp(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}