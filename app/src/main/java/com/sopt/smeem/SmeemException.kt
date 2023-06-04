package com.sopt.smeem

class SmeemException(
    val errorCode: SmeemErrorCode,
    val logMessage: String? = null
) : RuntimeException(if (logMessage.isNullOrEmpty()) errorCode.message else logMessage) {
}

enum class SmeemErrorCode(
    val code: Int,
    val message: String,
    val tip: String = "앱을 껐다 켜거나 나중에 다시 시도해 주세요.",
) {
    SYSTEM_ERROR(0, "시스템 오류가 발생했어요"),
    NETWORK_ERROR(1, "인터넷 연결을 확인해 주세요"),
    NETWORK_LOAD_ERROR(2, "데이터를 불러올 수 없어요")
}