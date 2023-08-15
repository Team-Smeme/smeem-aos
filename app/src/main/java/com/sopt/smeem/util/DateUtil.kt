package com.sopt.smeem.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.regex.Pattern

object DateUtil {
    /**
     * 0-24 를 0-12 로 변경
     */
    fun asHour(hour: Int) = if (hour < 13) hour else hour - 12

    /**
     * 0-24 를 ampm 으로 구분
     */
    fun asAmpm(hour: Int) = if (hour < 13) "AM" else "PM"

    private const val YYYY_MM_DD_HH_MM =
        "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]|60])"
    public const val YYYY_MM_DD =
        "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])"

    fun asLocalDateTime(yyyyMMddHHmm: String): LocalDateTime {
        if (Pattern.compile(YYYY_MM_DD_HH_MM).matcher(yyyyMMddHHmm).matches()) {
            return LocalDateTime.of(
                yyyyMMddHHmm.substring(0, 4).toInt(),
                yyyyMMddHHmm.substring(5, 7).toInt(),
                yyyyMMddHHmm.substring(8, 10).toInt(),
                yyyyMMddHHmm.substring(11, 13).toInt(),
                yyyyMMddHHmm.substring(14).toInt()
            ).plusHours(9)
        }

        throw IllegalArgumentException("wrong pattern matching for date")
    }

    fun asLocalDate(yyyyMMdd: String): LocalDate {
        if (Pattern.compile(YYYY_MM_DD).matcher(yyyyMMdd).matches()) {
            return LocalDate.of(
                yyyyMMdd.substring(0, 4).toInt(),
                yyyyMMdd.substring(5, 7).toInt(),
                yyyyMMdd.substring(8, 10).toInt()
            )
        }

        throw IllegalArgumentException("wrong pattern matching for date")
    }

    fun asString(date: LocalDateTime): String {
        val hour = if (date.hour / 10 >= 1) date.hour.toString() else "0${date.hour}"
        val minutes = if (date.minute < 10) "0${date.minute}" else date.minute.toString()

        return "${date.year}년 ${date.monthValue}월 ${date.dayOfMonth}일 ${hour}:${minutes}"
    }

    fun month(month: Int) =
        if (month <= 0) throw IllegalArgumentException("month can not be less than equal 0")
        else if (month < 10) "0$month"
        else "$month"

    fun day(day: Int) =
        if (day <= 0 || day > 31) throw IllegalArgumentException("day can not be less than equal 0 or greater than 31")
        else if (day < 10) "0$day"
        else "$day"

    fun yyyy_mm_dd(date: LocalDateTime): String =
        "${date.year}-${month(date.monthValue)}-${day(date.dayOfMonth)}"

    fun gap(start: String, end: String): Int =
        asLocalDate(end).compareTo(asLocalDate(start))

    object WithServer {
        fun asStringOnlyDate(now: LocalDate): String {
            val monthValue =
                if (now.monthValue < 10) "0${now.monthValue}" else now.monthValue.toString()
            return "${now.year}-${monthValue}-${now.dayOfMonth}"
        }

    }

    object WithUser {
        fun yearMonthDay(now: LocalDateTime): String {
            val monthValue =
                if (now.monthValue < 10) "0${now.monthValue}" else now.monthValue.toString()
            return "${now.year.toString().substring(2)}.${monthValue}.${now.dayOfMonth}"
        }

        fun hourMinute(createdAt: LocalDateTime): String {
            val toLocalTime = createdAt.toLocalTime()
            val hour = when {
                (toLocalTime.hour in 13..24) -> "%02d".format(toLocalTime.hour - 12)
                (toLocalTime.hour == 0) -> "12"
                else -> "%02d".format(toLocalTime.hour)
            }
            val minutes = "%02d".format(toLocalTime.minute)
            val ampm = if (toLocalTime.hour in 12..23) " PM" else " AM"

            return "$hour : $minutes $ampm"
        }
    }
}