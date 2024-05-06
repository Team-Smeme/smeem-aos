package com.sopt.smeem.data.usecase

import com.sopt.smeem.domain.dto.GetBadgeListDto
import com.sopt.smeem.domain.dto.MyPlanDto
import com.sopt.smeem.domain.dto.MySmeemDataDto
import com.sopt.smeem.domain.repository.UserRepository
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class MySummaryUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Triple<MySmeemDataDto, MyPlanDto?, List<GetBadgeListDto>> {
        val (smeemData, myPlan, badgeList) = coroutineScope {
            val smeemData = userRepository.getMySmeemData().data()
            val planResponse = userRepository.getMyPlanData().data()
            val badgeList = userRepository.getMyBadges().data()
            Triple(
                smeemData,
                planResponse.dto.takeUnless { planResponse.isResponseBodyNull },
                badgeList
            )
        }
        return Triple(smeemData, myPlan, badgeList)
    }
}