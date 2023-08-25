package com.sopt.smeem

enum class TrainingGoalType(val text: String) {
    DEVELOP("자기계발"),
    HOBBY("취미로 즐기기"),
    APPLY("현지 언어 체득"),
    BUSINESS("유창한 비즈니스 영어"),
    EXAM("어학 시험 고득점"),
    NONE("아직 모르겠어요"),
    NO_SELECTED(""),
    ;

    var id: Int = 0
    var selected: Boolean = false

    companion object {
        fun getAll(): Set<TrainingGoalType> = setOf(DEVELOP, HOBBY, APPLY, EXAM, BUSINESS, NONE)

        fun findById(id: Int) = getAll().find { it.id == id } ?: throw IllegalStateException()
        fun findByText(text: String) = getAll().find { it.text == text } ?: throw NoSuchElementException()
    }
}