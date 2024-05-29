package com.sopt.smeem.domain.dto

data class VersionDto(
    val title: String,
    val content: String,
    val iosVersion: Version,
    val androidVersion: Version
) {
    data class Version(
        val forceVersion: String? = null,
        val version: String
    )
}

