package com.sopt.smeem.data.datasource

import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.BadgeType
import com.sopt.smeem.domain.model.mypage.MyBadges

object BadgeList {

    /***** new - temp *****/
    val sprint2 = listOf<MyBadges>(
        MyBadges(
            name = "웰컴 배지",
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/120551217/6b3319cb-4c6f-4bf2-86dd-7576a44b46c7",
            description = "스밈에 오신 것을 환영해요!",
            hasObtained = true,
            userPercentage = 90
        ),
        MyBadges(
            name = "첫 일기",
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/120551217/10ed4dd9-276a-4344-87a8-f39b91deebd5",
            description = "시작이 좋아요!",
            hasObtained = true,
            userPercentage = 50
        ),
        MyBadges(
            name = "10번째 일기",
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/120551217/10ed4dd9-276a-4344-87a8-f39b91deebd5",
            description = "일기를 n번 더 작성해요",
            hasObtained = false,
            diariesLeftToObtain = 6,
            userPercentage = 30
        ),
        MyBadges(
            name = "10번째 일기",
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/120551217/10ed4dd9-276a-4344-87a8-f39b91deebd5",
            description = "일기를 n번 더 작성해요",
            hasObtained = false,
            diariesLeftToObtain = 26,
            userPercentage = 5
        ),
        MyBadges(
            name = "10번째 일기",
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/120551217/10ed4dd9-276a-4344-87a8-f39b91deebd5",
            description = "일기를 n번 더 작성해요",
            hasObtained = false,
            diariesLeftToObtain = 46,
            userPercentage = 10
        ),
        MyBadges(
            name = "3일 연속 일기",
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/120551217/3b26b274-722f-4dca-a560-e28c266efe69",
            description = "벌써 3일째! 좋은 습관을 만들어 보아요:)",
            hasObtained = true,
            userPercentage = 30
        ),
        MyBadges(
            name = "7일 연속 일기",
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/120551217/10ed4dd9-276a-4344-87a8-f39b91deebd5",
            description = "일기를 7일 연속으로 작성해요.",
            hasObtained = false,
            userPercentage = 30
        ),
        MyBadges(
            name = "7일 연속 일기",
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/120551217/10ed4dd9-276a-4344-87a8-f39b91deebd5",
            description = "일기를 15일 연속으로 작성해요.",
            hasObtained = false,
            userPercentage = 20
        ),
        MyBadges(
            name = "7일 연속 일기",
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/120551217/10ed4dd9-276a-4344-87a8-f39b91deebd5",
            description = "일기를 15일 연속으로 작성해요.",
            hasObtained = false,
            userPercentage = 10
        ),
    )

    /***** original *****/

    val event = listOf<Badge>(
        Badge(
            name = "웰컴 배지",
            type = BadgeType.EVENT,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/71878fa1-c447-4d0f-a8a7-32487a0b7617",
        )
    )

    val counting = listOf<Badge>(
        Badge(
            name = "첫 번째 일기",
            type = BadgeType.COUNTING,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/2bb58741-6ce4-4176-b65f-d324a58fb0c0",
        ),
        Badge(
            name = "열 번째 일기",
            type = BadgeType.COUNTING,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/00ea2db2-37de-46ac-808f-f83d5028e7c3",
        ),
        Badge(
            name = "30번째 일기",
            type = BadgeType.COUNTING,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/8dfaa8ac-8f72-4e9b-9b6e-444f22d1a79a",
        ),
        Badge(
            name = "50번째 일기",
            type = BadgeType.COUNTING,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/73bbb92d-4958-41be-8261-04cb18853ed9",
        )
    )

    val combo = listOf<Badge>(
        Badge(
            name = "3일 연속 일기",
            type = BadgeType.COMBO,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/35cca7d6-c9a1-41d0-b0a1-fae1c9810f4e",
        ),
        Badge(
            name = "7일 연속 일기",
            type = BadgeType.COMBO,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/199f1110-12e6-4851-9ce2-bc10ded79f6a",
        ),
        Badge(
            name = "15일 연속 일기",
            type = BadgeType.COMBO,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/90fb2e5b-7815-468b-a867-d500a1773f40",
        ),
        Badge(
            name = "30일 연속 일기",
            type = BadgeType.COMBO,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/0ca580d8-cbf4-4b07-9a35-9e1e19b4b0e7",
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
            name = "학습 목표 수정",
            type = BadgeType.EXPLORATION,
            imageUrl = "https://github.com/Team-Smeme/Smeme-plan/assets/74162198/cb792cd4-83a3-45c9-8027-b172d8b66bc2",
        ),
    )
}