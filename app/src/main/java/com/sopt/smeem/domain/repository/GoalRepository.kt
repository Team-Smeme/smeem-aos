package com.sopt.smeem.domain.repository

import com.sopt.smeem.StudyGoal
import com.sopt.smeem.domain.model.OnBoardingGoal

interface GoalRepository {
    suspend fun getGoalDetail(studyGoal: StudyGoal): Result<OnBoardingGoal>
}