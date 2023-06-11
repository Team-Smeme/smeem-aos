package com.sopt.smeem

enum class StudyGoal {
    // TODO : server enum 에 맞게 네이밍 수정 필요
    SELF_IMPROVEMENT, // 자기 계발
    HOBBY, // 취미
    NATIVE, // 현지 언어 적응
    BUSINESS, // 업무 활용
    READ_AND_WRITE, // 시험 고득점
    NO_IDEA, // 아직 모르겠어요
    NONE, // 아무 값도 아님
    ;


    var id: Int = 0
    var selected: Boolean = false

    companion object {
        fun getAll(): Set<StudyGoal> = setOf(SELF_IMPROVEMENT, HOBBY, NATIVE, READ_AND_WRITE, BUSINESS, NO_IDEA)

        fun findById(id: Int) = getAll().find { it.id == id } ?: throw IllegalStateException()
    }
}