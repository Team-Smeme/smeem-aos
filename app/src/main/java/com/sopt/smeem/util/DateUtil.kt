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

    fun asObject(yyyyMMddHHmm: String): LocalDateTime {
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

    fun asString(date: LocalDateTime): String {
        val hour = if (date.hour / 10 >= 1) date.hour.toString() else "0${date.hour}"
        val minutes = if (date.minute < 10) "0${date.minute}" else date.minute.toString()

        return "${date.year}년 ${date.monthValue}월 ${date.dayOfMonth}일 ${hour}:${minutes}"
    }

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
            val hour =
                if (toLocalTime.hour / 10 >= 1) toLocalTime.hour.toString() else "0${toLocalTime.hour}"
            val minutes =
                if (toLocalTime.minute < 10) "0${toLocalTime.minute}" else toLocalTime.minute.toString()

            return "$hour:$minutes"
        }
    }
}