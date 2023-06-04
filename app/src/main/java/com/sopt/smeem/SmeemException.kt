package com.sopt.smeem

import android.util.Log

class SmeemException(
    val errorCode: SmeemErrorCode,
    val logMessage: String? = null,
    val throwable: Throwable
) : RuntimeException(if (logMessage.isNullOrEmpty()) errorCode.message else logMessage) {
}

enum class SmeemErrorCode(
    val code: Int,
    val message: String,
    val tip: String = "앱을 껐다 켜거나 나중에 다시 시도해 주세요.",
) {
    SYSTEM_ERROR(0, "시스템 오류가 발생했어요"),
    NETWORK_ERROR(1, "인터넷 연결을 확인해 주세요"),
    NETWORK_LOAD_ERROR(2, "데이터를 불러올 수 없어요"),
    CLIENT_ERROR(3, "잘못된 접근입니다."),
    FORBIDDEN(4, "권한이 필요한 접근입니다."),
    UNAUTHORIZED(5, "인증이 필요한 접근입니다."),
    UNKNOWN_ERROR(6, "알 수 없는 에러가 발생했습니다.")
}

fun SmeemException.description() = this.errorCode.message
fun SmeemException.tip() = this.errorCode.tip
fun SmeemException.logging(tag: String) =
    Log.e(tag, this.logMessage ?: this.description(), this.throwable)