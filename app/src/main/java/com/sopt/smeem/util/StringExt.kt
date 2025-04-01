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