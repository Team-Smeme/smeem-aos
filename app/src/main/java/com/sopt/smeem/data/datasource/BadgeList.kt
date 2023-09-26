package com.sopt.smeem.data.datasource

import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.BadgeType

object BadgeList {
    val event = listOf<Badge>(
        Badge(
            badgeId = 1,
            title = "웰컴 배지",
            badgeType = BadgeType.EVENT,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/71878fa1-c447-4d0f-a8a7-32487a0b7617",
            description = null
        )
    )

    val counting = listOf<Badge>(
        Badge(
            badgeId = 2,
            title = "첫 번째 일기",
            badgeType = BadgeType.COUNTING,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/2bb58741-6ce4-4176-b65f-d324a58fb0c0",
            description = null
        ),
        Badge(
            badgeId = 3,
            title = "열 번째 일기",
            badgeType = BadgeType.COUNTING,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/00ea2db2-37de-46ac-808f-f83d5028e7c3",
            description = null
        ),
        Badge(
            badgeId = 4,
            title = "30번째 일기",
            badgeType = BadgeType.COUNTING,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/8dfaa8ac-8f72-4e9b-9b6e-444f22d1a79a",
            description = null
        ),
        Badge(
            badgeId = 5,
            title = "50번째 일기",
            badgeType = BadgeType.COUNTING,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/73bbb92d-4958-41be-8261-04cb18853ed9",
            description = null
        )
    )

    val combo = listOf<Badge>(
        Badge(
            badgeId = 6,
            title = "3일 연속 일기",
            badgeType = BadgeType.COMBO,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/35cca7d6-c9a1-41d0-b0a1-fae1c9810f4e",
            description = null
        ),
        Badge(
            badgeId = 7,
            title = "7일 연속 일기",
            badgeType = BadgeType.COMBO,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/199f1110-12e6-4851-9ce2-bc10ded79f6a",
            description = null
        ),
        Badge(
            badgeId = 8,
            title = "15일 연속 일기",
            badgeType = BadgeType.COMBO,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/90fb2e5b-7815-468b-a867-d500a1773f40",
            description = null
        ),
        Badge(
            badgeId = 9,
            title = "30일 연속 일기",
            badgeType = BadgeType.COMBO,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/0ca580d8-cbf4-4b07-9a35-9e1e19b4b0e7",
            description = null
        ),
    )

    val exploration = listOf<Badge>(
//        Badge(
//            badgeId = 10,
//            title = "첫 번째 첨삭",
//            badgeType = BadgeType.EXPLORATION,
//            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/f18268ad-4c2b-4792-b1f8-ed56086f952a",
//            description = null
//        ),
//        Badge(
//            badgeId = 11,
//            title = "3개 이상 첨삭",
//            badgeType = BadgeType.EXPLORATION,
//            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/434e1eca-d1ab-4d3b-8f2f-ecf2ffa59ebc",
//            description = null
//        ),
//        Badge(
//            badgeId = 12,
//            title = "5개 이상 첨삭",
//            badgeType = BadgeType.EXPLORATION,
//            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/513cbb2d-19c1-404d-ad97-693783913649",
//            description = null
//        ),
        Badge(
            badgeId = 13,
            title = "학습 목표 수정",
            badgeType = BadgeType.EXPLORATION,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/cb792cd4-83a3-45c9-8027-b172d8b66bc2",
            description = null
        ),
    )
}