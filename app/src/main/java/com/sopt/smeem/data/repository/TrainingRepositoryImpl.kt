package com.sopt.smeem.data.repository

import com.sopt.smeem.SmeemErrorCode
import com.sopt.smeem.SmeemException
import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.data.datasource.TrainingManager
import com.sopt.smeem.domain.model.TrainingGoal
import com.sopt.smeem.domain.model.TrainingGoalSimple
import com.sopt.smeem.domain.repository.TrainingRepository

class TrainingRepositoryImpl(
    private val trainingManager: TrainingManager
) : TrainingRepository {
    override suspend fun getDetail(goal: TrainingGoalType?): Result<TrainingGoal> =
        kotlin.runCatching {
            trainingManager.getDetail(
                goal ?: throw SmeemException(
                    errorCode = SmeemErrorCode.SYSTEM_ERROR,
                    logMessage = "없는 트레이닝 목표에 대한 접근이 발생했습니다.",
                    throwable = IllegalArgumentException()
                )
            )
        }
            .map {
                TrainingGoal(
                    goal = it.data!!.name,
                    way = it.data.way,
                    detail = it.data.detail
                )
            }

    override suspend fun getAll(): Result<TrainingGoalSimple> =
        kotlin.runCatching {
            trainingManager.getAll()
        }.map {
            TrainingGoalSimple(
                goalType = TrainingGoalType.valueOf(it.data!!.goalType),
                name = it.data.name
            )
        }
}