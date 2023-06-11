package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.OnBoarding
import com.sopt.smeem.domain.model.auth.Joining

interface UserRepository {
    /**
     * OnBoarding 결과 (학습 목표, 알람 여부, 알람 시간) 를 서버로 전송합니다.
     */
    suspend fun patchOnBoarding(onBoarding: OnBoarding): Result<Unit>

    /**
     * 닉네임 및 이용약관 정보를 서버로 전송합니다.
     */
    suspend fun patchNicknameAndAcceptance(
        nickname: String,
        marketingAcceptance: Boolean? = null
    ): Result<Joining>
}