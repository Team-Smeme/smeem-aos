package com.sopt.smeem.domain.model.mypage

data class MyBadges(
    val name: String,
    val imageUrl: String,
    val description: String,
    val hasObtained: Boolean,
    val diariesLeftToObtain: Int? = null,
    val userPercentage: Int,
)
