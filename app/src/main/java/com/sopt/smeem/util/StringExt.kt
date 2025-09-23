package com.sopt.smeem.util

import java.text.BreakIterator

fun String.graphemeLength(): Int {
    if (this.isEmpty()) return 0

    val iterator = BreakIterator.getCharacterInstance()
    iterator.setText(this)

    var count = 0
    var start = iterator.first()

    while (start != BreakIterator.DONE) {
        val end = iterator.next()
        if (end == BreakIterator.DONE) break
        count++
        start = end
    }

    return count
}

fun String.extractMainContent(): String {
    // 패턴: ": \"본문 내용\"." 부분에서 본문만 추출
    val startIndex = this.indexOf(": \"")
    val endIndex = this.lastIndexOf("\".")

    return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
        this.substring(startIndex + 3, endIndex).trim()
    } else {
        this // 패턴이 맞지 않으면 원본 반환
    }
}