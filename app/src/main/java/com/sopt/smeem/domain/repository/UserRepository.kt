package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.ApiResult
import com.sopt.smeem.domain.dto.GetBadgeListDto
import com.sopt.smeem.domain.dto.LoginResultDto
import com.sopt.smeem.domain.dto.MyInfoDto
import com.sopt.smeem.domain.dto.MyPlanDtoWrapper
import com.sopt.smeem.domain.dto.MySmeemDataDto
import com.sopt.smeem.domain.dto.PostOnBoardingDto
import com.sopt.smeem.domain.model.PushAlarm
import com.sopt.smeem.domain.model.Training

interface UserRepository {

    suspend fun registerOnBoarding(
        onBoardingDto: PostOnBoardingDto,
        loginResult: LoginResultDto
    ): ApiResult<Unit>

    suspend fun modifyUsername(nickname: String): ApiResult<Boolean>

    suspend fun registerUserInfo(
        accessToken: String,
        nickname: String,
        marketingAcceptance: Boolean
    ): ApiResult<Boolean>

    suspend fun getMySmeemData(): ApiResult<MySmeemDataDto>

    suspend fun getMyPlanData(): ApiResult<MyPlanDtoWrapper>

    suspend fun getMyInfo(): ApiResult<MyInfoDto>

    suspend fun registerTraining(accessToken: String, training: Training): ApiResult<Unit>
    suspend fun editTraining(training: Training): ApiResult<Unit>
    suspend fun registerPushAlarm(accessToken: String, push: PushAlarm): ApiResult<Unit>
    suspend fun editPushAlarm(push: PushAlarm): ApiResult<Unit>
    suspend fun deleteUser(): ApiResult<Unit>
    suspend fun getMyBadges(): ApiResult<List<GetBadgeListDto>>
    suspend fun activeVisit(): ApiResult<Unit>
}