package com.sopt.smeem.util

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField

object TextUtil {
    fun String.limitLengthLessThan601() {
        if (this.length <= 600) this
        else "${this.substring(0, 601)} ... (${this.length})"
    }

    fun String.toLocalDateTime(): LocalDateTime {
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm[:ss]")
            .appendFraction(ChronoField.MILLI_OF_SECOND, 0, 9, true)
            .toFormatter()

        return LocalDateTime.parse(this, formatter)
    }
}