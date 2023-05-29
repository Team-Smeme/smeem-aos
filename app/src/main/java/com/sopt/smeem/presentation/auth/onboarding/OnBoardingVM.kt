package com.sopt.smeem.presentation.auth.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sopt.smeem.Day

class OnBoardingVM : ViewModel() {
    private val _selectedGoal = MutableLiveData<GoalSelection>()
    val selectedGoal: LiveData<GoalSelection>
        get() = _selectedGoal
    private val _setTimeLater = MutableLiveData<Boolean>()
    val setTimeLater: LiveData<Boolean>
        get() = _setTimeLater

    var step: Int = 0
    val days = mutableListOf<Day>()
    var hour: Int = 0
    var minute: Int = 0

    fun goToNext() {
        step++
    }

    fun isDaySelected(content: String) = days.contains(Day.from(content))
    fun addDay(content: String) = days.add(Day.from(content))
    fun removeDay(content: String) = days.remove(Day.from(content))
    fun upsert(target: GoalSelection) {
        if (selectedGoal.value == target) {
            _selectedGoal.value!!.selected = false
            _selectedGoal.value = GoalSelection.NONE
        } else {
            _selectedGoal.value = target
            _selectedGoal.value!!.selected = true
        }
    }

    fun none() {
        _selectedGoal.value = GoalSelection.NONE
    }

    fun timeLater() {
        _setTimeLater.value = true
    }
}

enum class GoalSelection(
    val title: String = "",
    val howTo: String = "",
    val goalDetail: String = ""
) {
    SELF_IMPROVEMENT("자기계발"),
    HOBBY("취미로 즐기기"),
    NATIVE("현지 언어에 적응하기"),
    EXAM("외국어 시험 고득점하기"),
    READ(
        title = "외국어 원서 독해",
        howTo = """
        >주 3회 이상 일기 작성하기
        >편지글 형태의 일기 작성하기
        """.trimMargin(">"),
        goalDetail = """
        >사전 보지 않고 일기 작성하기
        >TOEIC 단어책 뭐뭐하기
        """.trimMargin(">")
    ),
    NO_IDEA("아직 모르겠어요"),
    NONE
    ;

    var id: Int = 0
    var selected: Boolean = false

    companion object {
        fun getAll(): Set<GoalSelection> =
            setOf(SELF_IMPROVEMENT, HOBBY, NATIVE, EXAM, READ, NO_IDEA)

        fun findById(id: Int) = getAll().find { it.id == id } ?: throw IllegalStateException()
    }
}