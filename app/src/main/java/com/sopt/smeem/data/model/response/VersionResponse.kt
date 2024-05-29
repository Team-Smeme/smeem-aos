package com.sopt.smeem.data.model.response


data class VersionResponse(
    val title: String,
    val content: String,
    val iosVersion: Version,
    val androidVersion: Version
) {
    data class Version(
        val forceVersion: String?,
        val version: String
    )
}
