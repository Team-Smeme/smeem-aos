package com.sopt.smeem.presentation.home

data class ConfigInfo(
    val bannerVersion: Int = 0,
    val bannerTitle: String = "",
    val bannerContent: String = "",
    val isBannerEnabled: Boolean = false,
    val isExternalEvent: Boolean = false,
    val bannerEventPath: String = "",
)
