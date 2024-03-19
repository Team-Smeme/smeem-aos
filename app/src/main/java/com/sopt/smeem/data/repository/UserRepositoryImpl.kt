package com.sopt.smeem.data.repository

import com.sopt.smeem.LanguageCode
import com.sopt.smeem.data.datasource.MyBadgeRetriever
import com.sopt.smeem.data.datasource.MyPageRetriever
import com.sopt.smeem.data.datasource.TrainingManager
import com.sopt.smeem.data.datasource.UserModifier
import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.domain.model.LoginResult
import com.sopt.smeem.domain.model.MyPage
import com.sopt.smeem.domain.model.MyPageBadge
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.PushAlarm
import com.sopt.smeem.domain.model.Training
import com.sopt.smeem.domain.model.TrainingGoal
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.domain.repository.UserRepository

class UserRepositoryImpl(
    private val trainingManager: TrainingManager,
    private val userModifier: UserModifier,
    private val myPageRetriever: MyPageRetriever,
    private val myBadgeRetriever: MyBadgeRetriever,
) : UserRepository {
    override suspend fun registerOnBoarding(
        onBoarding: OnBoarding,
        loginResult: LoginResult
    ): Result<Unit> =
        kotlin.runCatching { trainingManager.registerOnBoarding(onBoarding, loginResult) }

    override suspend fun modifyUserInfo(
        accessToken: String?,
        username: String,
        marketingAcceptance: Boolean?
    ): Result<Boolean> =
        kotlin.runCatching {
            userModifier.patch(
                accessToken = accessToken,
                username = username,
                marketingAcceptance = marketingAcceptance
            )
        }.map { true }

    override suspend fun getMyPage(): Result<MyPage> =
        kotlin.runCatching {
            myPageRetriever.getResponse()
        }.map { response ->
            MyPage(
                username = response.data!!.username,
                myPageBadge = MyPageBadge.from(response.data.badge),
                hasPushAlarm = response.data.hasPushAlarm,
                goal = TrainingGoal(
                    title = response.data.title,
                    goal = response.data.target,
                    detail = response.data.detail ?: "",
                    way = response.data.way ?: ""
                ),
                language = LanguageCode.en.language,
                trainingTime = TrainingTime(
                    days = response.data.trainingTime!!.day.let {
                        if (it.isNotBlank()) {
                            it.split(",")
                                .map { Day.valueOf(it) }
                                .toSet()
                        } else emptySet()
                    } ?: emptySet(),
                    hour = response.data.trainingTime.hour,
                    minute = response.data.trainingTime.minute
                )
            )
        }

    override suspend fun getMyBadges(): Result<List<Badge>> =
        kotlin.runCatching {
            myBadgeRetriever.getResponse()
        }.map { response ->
            response.data?.badgeTypes
                ?.flatMap { it.badges }
                ?.map { badgeResponse ->
                    Badge(
                        name = badgeResponse.name,
                        imageUrl = badgeResponse.imageUrl,
                        type = badgeResponse.type,
                    )
                }
                ?: throw IllegalArgumentException("내부 로직 구현 오류")
        }

    override suspend fun editTraining(accessToken: String?, training: Training): Result<Unit> =
        kotlin.runCatching {
            trainingManager.patchTraining(accessToken, training)
        }

    override suspend fun editPushAlarm(accessToken: String?, push: PushAlarm): Result<Unit> =
        kotlin.runCatching {
            trainingManager.patchPushAlarm(accessToken, push)
        }

    override suspend fun deleteUser(): Result<Unit> =
        kotlin.runCatching {
            userModifier.delete()
        }
}