package com.sopt.smeem.domain.model

enum class Day(private val korean: String) {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일"),
    ;

    companion object {
        fun from(ko: String): Day = values().first { it.korean == ko }
    }
}