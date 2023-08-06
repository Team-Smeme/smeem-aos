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
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.Training
import com.sopt.smeem.domain.model.TrainingGoal
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.domain.repository.UserRepository

class UserRepositoryImpl(
    private val trainingManager: TrainingManager? = null,
    private val userModifier: UserModifier? = null,
    private val myPageRetriever: MyPageRetriever? = null,
    private val myBadgeRetriever: MyBadgeRetriever? = null,
) : UserRepository {
    override suspend fun registerOnBoarding(
        onBoarding: OnBoarding,
        loginResult: LoginResult
    ): Result<Unit> =
        kotlin.runCatching { trainingManager!!.registerOnBoarding(onBoarding, loginResult) }

    override suspend fun modifyUserInfo(
        username: String,
        marketingAcceptance: Boolean?
    ): Result<Boolean> =
        kotlin.runCatching {
            userModifier!!.patch(
                username = username,
                marketingAcceptance = marketingAcceptance
            )
        }.map { true }

    override suspend fun getMyPage(): Result<MyPage> =
        kotlin.runCatching {
            myPageRetriever!!.getResponse()
        }.map { response ->
            MyPage(
                username = response.data!!.username,
                badge = Badge.from(response.data.badge),
                hasPushAlarm = response.data.hasPushAlarm,
                goal = TrainingGoal(
                    goal = response.data.target,
                    detail = response.data.detail ?: "",
                    way = response.data.way ?: ""
                ),
                language = LanguageCode.en.language,
                trainingTime = TrainingTime(
                    days = response.data.trainingTime!!.day?.let {
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
            myBadgeRetriever!!.getResponse()
        }.map { response ->
            response.data!!.badges.map { badgeResponse ->
                Badge(
                    badgeId = badgeResponse.id,
                    title = badgeResponse.name,
                    description = badgeResponse.description,
                    imageUrl = badgeResponse.imageUrl,
                    badgeType = badgeResponse.type
                )
            }
        }

    override suspend fun editTraining(training: Training): Result<Unit> =
        kotlin.runCatching {
            trainingManager!!.patchTraining(training)
        }
}