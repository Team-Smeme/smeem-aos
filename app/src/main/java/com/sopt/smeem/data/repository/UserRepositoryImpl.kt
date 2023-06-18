package com.sopt.smeem.data.repository

import com.sopt.smeem.LanguageCode
import com.sopt.smeem.data.datasource.JoinHelper
import com.sopt.smeem.data.datasource.MyPageRetriever
import com.sopt.smeem.data.datasource.PlanSetter
import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.Joining
import com.sopt.smeem.domain.model.MyPage
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.OnBoardingGoal
import com.sopt.smeem.domain.model.Training
import com.sopt.smeem.domain.repository.UserRepository

class UserRepositoryImpl(
    private val planSetter: PlanSetter? = null,
    private val joinHelper: JoinHelper? = null,
    private val myPageRetriever: MyPageRetriever? = null
) : UserRepository {
    override suspend fun patchOnBoarding(onBoarding: OnBoarding): Result<Unit> =
        kotlin.runCatching { planSetter!!.patch(onBoarding) }

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
                badge = Badge.from(response.data.badge),
                hasPushAlarm = response.data.hasPushAlarm,
                goal = OnBoardingGoal(
                    goal = response.data.target,
                    goalDetail = response.data.targetDetail ?: "",
                    howTo = response.data.targetHowTo ?: ""
                ),
                language = LanguageCode.en.language,
                training = Training(
                    days = response.data.trainingTime.day,
                    hour = response.data.trainingTime.hour.toInt(),
                    minute = response.data.trainingTime.minute.toInt()
                )
            )
        }
}