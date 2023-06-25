package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.Joining
import com.sopt.smeem.domain.model.MyPage
import com.sopt.smeem.domain.model.Training

interface UserRepository {
    /**
     * OnBoarding 결과 (학습 목표, 알람 여부, 알람 시간) 를 서버로 전송합니다.
     */
    suspend fun registerOnBoarding(onBoarding: OnBoarding): Result<Unit>

    /**
     * 닉네임 및 이용약관 정보를 서버로 전송합니다.
     */
    suspend fun patchNicknameAndAcceptance(
        nickname: String,
        marketingAcceptance: Boolean? = null
    ): Result<Joining>

    suspend fun getMyPage() : Result<MyPage>

    /**
     * 나의 뱃지 정보를 조회합니다.
     */
    suspend fun getMyBadges() : Result<List<Badge>>

    /**
     * 트레이닝 정보를 수정합니다.
     */
    suspend fun editTraining(training: Training): Result<Unit>
}