package com.sopt.smeem.data.datasource

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

object DummyDate {
    private val _diaryDateList = MutableStateFlow<List<LocalDate>>(
        listOf(
            LocalDate.of(2023, 10, 9),
            LocalDate.of(2023, 10, 19),
            LocalDate.of(2023, 10, 20),
            LocalDate.of(2023, 10, 21),
            LocalDate.of(2023, 10, 26),
            LocalDate.of(2023, 10, 28),
            LocalDate.of(2023, 11, 2),
            LocalDate.of(2023, 11, 10),
            LocalDate.of(2023, 11, 12),
            LocalDate.of(2023, 11, 22),
            LocalDate.of(2023, 12, 5),
            LocalDate.of(2023, 12, 17),
            LocalDate.of(2023, 12, 22),
            LocalDate.of(2023, 12, 25),
            LocalDate.of(2023, 12, 29),
            LocalDate.of(2023, 12, 31),
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 1, 8),
            LocalDate.of(2024, 1, 12),
            LocalDate.of(2024, 1, 13),
            LocalDate.of(2024, 1, 19),
            LocalDate.of(2024, 1, 20),
            LocalDate.of(2024, 1, 21),
            LocalDate.of(2024, 1, 22),
        )
    )
    val diaryDateList: StateFlow<List<LocalDate>> = _diaryDateList
}