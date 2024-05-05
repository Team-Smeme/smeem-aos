package com.sopt.smeem.data.usecase

import com.sopt.smeem.domain.dto.MyPlanDto
import com.sopt.smeem.domain.dto.MySmeemDataDto
import com.sopt.smeem.domain.repository.UserRepository
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class MySummaryUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    // TODO : 배지까지 반영 -> 주석 해제
//    suspend operator fun invoke(): Triple<MySmeemDataDto, MyPlanDto?, List<GetBadgeListDto>> =
//        coroutineScope {
//            val smeemData = userRepository.getMySmeemData().data()
//            val myPlan = userRepository.getMyPlanData().data()
//            val badgeList = userRepository.getMyBadges().data()
//            Triple(smeemData, myPlan, badgeList)
//        }

    suspend operator fun invoke(): Pair<MySmeemDataDto, MyPlanDto?> {
        val (smeemData, myPlan) = coroutineScope {
            val smeemData = userRepository.getMySmeemData().data()
            val response = userRepository.getMyPlanData().data()
            Pair(smeemData, response.dto.takeUnless { response.isResponseBodyNull })
        }

        return Pair(smeemData, myPlan)
    }
}