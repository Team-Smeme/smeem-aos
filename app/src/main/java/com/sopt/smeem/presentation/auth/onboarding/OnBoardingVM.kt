package com.sopt.smeem.presentation.auth.onboarding

import androidx.lifecycle.ViewModel
import com.sopt.smeem.Day

class OnBoardingVM : ViewModel() {
    var selectedGoal: Int = -1
    var step: Int = 0
    val days = mutableListOf<Day>()
    var hour : Int = 0
    var minute : Int = 0

    fun goToNext() {
        step++
    }

    fun isGoalSelected() = selectedGoal >= 0
    fun goalInit() {
        selectedGoal = -1
    }

    fun isDaySelected(content: String) = days.contains(Day.from(content))
    fun addDay(content: String) = days.add(Day.from(content))
    fun removeDay(content: String) = days.remove(Day.from(content))
}