package com.sopt.smeem.util

object TextUtil {
    fun String.limitLengthLessThan601() {
        if (this.length <= 600) this
        else "${this.substring(0, 601)} ... (${this.length})"
    }
}