package com.sopt.smeem.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

// 화면 전체
fun View.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
) {
    Snackbar
        .make(this, message, duration)
        .show()
}

// 해당 뷰 위로 올릴 때
fun View.showSnackbar(
    message: String,
    anchor: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
) {
    Snackbar
        .make(this, message, duration)
        .setAnchorView(anchor)
        .show()
}