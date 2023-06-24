package com.sopt.smeem

enum class TrainingGoalType {
    DEVELOP, // 자기 계발
    HOBBY, // 취미
    APPLY, // 현지 언어 적응
    BUSINESS, // 업무 활용
    EXAM, // 시험 고득점
    NONE, // 아직 모르겠어요
    NO_IDEA, // 아무 값도 아님
    ;

    var id: Int = 0
    var selected: Boolean = false

    companion object {
        fun getAll(): Set<TrainingGoalType> = setOf(DEVELOP, HOBBY, APPLY, EXAM, BUSINESS, NONE)

        fun findById(id: Int) = getAll().find { it.id == id } ?: throw IllegalStateException()
    }
}