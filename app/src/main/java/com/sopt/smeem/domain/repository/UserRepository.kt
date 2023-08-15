package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.LoginResult
import com.sopt.smeem.domain.model.MyPage
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.Training

interface UserRepository {
    /**
     * OnBoarding 결과 (학습 목표, 알람 여부, 알람 시간) 를 서버로 전송합니다. (사전에 로그인 없이 진입한 상태)
     */
    suspend fun registerOnBoarding(onBoarding: OnBoarding, loginResult: LoginResult): Result<Unit>

    /**
     * 닉네임 및 이용약관 정보를 서버로 전송합니다.
     */
    suspend fun modifyUserInfo(
        accessToken: String? = null,
        nickname: String,
        marketingAcceptance: Boolean? = null
    ): Result<Boolean>

    suspend fun getMyPage(): Result<MyPage>

    /**
     * 나의 뱃지 정보를 조회합니다.
     */
    suspend fun getMyBadges(): Result<List<Badge>>

    /**
     * 트레이닝 정보를 수정합니다.
     */
    suspend fun editTraining(accessToken: String? = null, training: Training): Result<Unit>

    suspend fun deleteUser(): Result<Unit>
}