package com.sopt.smeem.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun String.dayNameParseToAlphabet(): String = when (this) {
    "SUNDAY" -> "S"
    "MONDAY" -> "M"
    "TUESDAY" -> "T"
    "WEDNESDAY" -> "W"
    "THURSDAY" -> "T"
    "FRIDAY" -> "F"
    "SATURDAY" -> "S"
    else -> throw IllegalStateException("This is not day String")
}

fun Date.isTheSameDay(comparedDate: Date): Boolean {
    val calendar = Calendar.getInstance()
    calendar.withTime(this)
    val comparedCalendarDate = Calendar.getInstance()
    comparedCalendarDate.withTime(comparedDate)
    return calendar.get(Calendar.DAY_OF_YEAR) == comparedCalendarDate.get(Calendar.DAY_OF_YEAR) &&
        calendar.get(Calendar.YEAR) == comparedCalendarDate.get(Calendar.YEAR)
}

fun Calendar.withTime(date: Date) {
    clear()
    time = date
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun Calendar.toPrettyMonthString(
    style: Int = Calendar.LONG,
    locale: Locale = Locale.KOREA,
): String {
    val month = getDisplayName(Calendar.MONTH, style, locale)
    val year = get(Calendar.YEAR).toString()
    require(month != null) { throw IllegalStateException("Cannot get pretty name") }
    return if (locale.country.equals(Locale.KOREA.country)) {
        "${year}년 $month"
    } else {
        "$year $month"
    }
}

fun Calendar.toPrettyDateString(): String {
    val month = (get(Calendar.MONTH) + 1).toString()
    val year = get(Calendar.YEAR).toString()
    val day = get(Calendar.DAY_OF_MONTH).toString()
    return "$year.$month.$day"
}

fun Calendar.isWeekend(): Boolean {
    return get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
}

@SuppressLint("SimpleDateFormat")
fun String.achievementConvertStringToDate(): Date? {
    val format = SimpleDateFormat("yyyy.MM.dd")
    return runCatching {
        format.parse(this)
    }.getOrNull()
}
