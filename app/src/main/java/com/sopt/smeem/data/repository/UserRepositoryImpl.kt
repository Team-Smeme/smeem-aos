package com.sopt.smeem.data.repository

import com.sopt.smeem.LanguageCode
import com.sopt.smeem.data.datasource.JoinHelper
import com.sopt.smeem.data.datasource.MyBadgeRetriever
import com.sopt.smeem.data.datasource.MyPageRetriever
import com.sopt.smeem.data.datasource.TrainingManager
import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.Joining
import com.sopt.smeem.domain.model.MyPage
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.Training
import com.sopt.smeem.domain.model.TrainingGoal
import com.sopt.smeem.domain.model.TrainingTime
import com.sopt.smeem.domain.repository.UserRepository

class UserRepositoryImpl(
    private val trainingManager: TrainingManager? = null,
    private val joinHelper: JoinHelper? = null,
    private val myPageRetriever: MyPageRetriever? = null,
    private val myBadgeRetriever: MyBadgeRetriever? = null,
) : UserRepository {
    override suspend fun registerOnBoarding(onBoarding: OnBoarding): Result<Unit> =
        kotlin.runCatching { trainingManager!!.registerOnBoarding(onBoarding) }

    override suspend fun patchNicknameAndAcceptance(
        username: String,
        marketingAcceptance: Boolean?
    ): Result<Joining> =
        kotlin.runCatching {
            joinHelper!!.patch(
                username = username,
                marketingAcceptance = marketingAcceptance
            )
        }.map { response ->
            Joining(
                username = response.data!!.username,
                marketingAcceptance = response.data.ternAccepted
            )
        }

    override suspend fun getMyPage(): Result<MyPage> =
        kotlin.runCatching {
            myPageRetriever!!.getResponse()
        }.map { response ->
            MyPage(
                username = response.data!!.username,
                badge = Badge.from(response.data.badges[0]),
                hasPushAlarm = response.data.hasPushAlarm,
                goal = TrainingGoal(
                    goal = response.data.target,
                    detail = response.data.detail ?: "",
                    way = response.data.way ?: ""
                ),
                language = LanguageCode.en.language,
                trainingTime = TrainingTime(
                    days = response.data.trainingTime.day,
                    hour = response.data.trainingTime.hour.toInt(),
                    minute = response.data.trainingTime.minute.toInt()
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