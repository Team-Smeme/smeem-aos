package com.sopt.smeem.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sopt.smeem.Authenticated
import com.sopt.smeem.SmeemException
import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.BadgeType
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BadgeViewModel @Inject constructor() : ViewModel() {

    @Inject
    @Authenticated
    lateinit var userRepository: UserRepository

    private val _badges: MutableLiveData<Map<BadgeType, List<Badge>>> = MutableLiveData()
    val badges: LiveData<Map<BadgeType, List<Badge>>>
        get() = _badges

    fun getBadges(
        onError: (SmeemException) -> Unit
    ) {
        // TODO : 서버 spec 일치 후에 확인
        /*        viewModelScope.launch {
                    userRepository.getMyBadges()
                        .onSuccess {
                            _badges.value = it.groupBy(keySelector = Badge::badgeType)
                        }
                        .onHttpFailure { e -> onError(e) }
                }*/
        _badges.value = mapOf(
            BadgeType.WELCOME to listOf(
                Badge(
                    badgeId = 1,
                    title = "환영 뱃지",
                    description = "첫 인사를 환영합니다!",
                    badgeType = BadgeType.WELCOME,
                    imageUrl = "https://github-production-user-asset-6210df.s3.amazonaws.com/120551217/244917999-f9f63817-8b1a-483c-b0e1-4935c96e2d03.png"
                )
            ),
            BadgeType.ACCUMULATED to listOf(
                Badge(
                    badgeId = 1,
                    title = "누적 뱃지",
                    description = "첫 인사를 환영합니다!",
                    badgeType = BadgeType.ACCUMULATED,
                    imageUrl = "https://github-production-user-asset-6210df.s3.amazonaws.com/120551217/244917999-f9f63817-8b1a-483c-b0e1-4935c96e2d03.png"
                )
            ),
            BadgeType.CONTINUED to listOf(
                Badge(
                    badgeId = 1,
                    title = "연속 뱃지",
                    description = "첫 인사를 환영합니다!",
                    badgeType = BadgeType.CONTINUED,
                    imageUrl = "https://github-production-user-asset-6210df.s3.amazonaws.com/120551217/244917999-f9f63817-8b1a-483c-b0e1-4935c96e2d03.png"
                )
            ),
            BadgeType.EXTRA to listOf(
                Badge(
                    badgeId = 1,
                    title = "기타 뱃지 1",
                    description = "첫 인사를 환영합니다!",
                    badgeType = BadgeType.EXTRA,
                    imageUrl = "https://github-production-user-asset-6210df.s3.amazonaws.com/120551217/244917999-f9f63817-8b1a-483c-b0e1-4935c96e2d03.png"
                ),
                Badge(
                    badgeId = 1,
                    title = "기타 뱃지 2",
                    description = "첫 인사를 환영합니다!",
                    badgeType = BadgeType.EXTRA,
                    imageUrl = "https://github-production-user-asset-6210df.s3.amazonaws.com/120551217/244917999-f9f63817-8b1a-483c-b0e1-4935c96e2d03.png"
                )
            ),
        )
    }
}