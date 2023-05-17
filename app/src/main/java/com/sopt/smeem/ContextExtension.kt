package com.sopt.smeem

import android.content.Context

/** dp size to pixel size **/
fun Context.dpToPx(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale).toInt()
}