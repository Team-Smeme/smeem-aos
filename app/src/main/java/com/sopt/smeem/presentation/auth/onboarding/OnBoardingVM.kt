package com.sopt.smeem.presentation.auth.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sopt.smeem.Day

class OnBoardingVM : ViewModel() {
    private val _selectedGoal = MutableLiveData<GoalSelection>()
    val selectedGoal: LiveData<GoalSelection>
        get() = _selectedGoal

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
}

enum class GoalSelection {
    SELF_IMPROVEMENT,
    HOBBY,
    NATIVE,
    EXAM,
    READ,
    NO_IDEA,
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