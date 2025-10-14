package com.sopt.smeem.domain.common

import android.util.Log

class SmeemException(
    val errorCode: SmeemErrorCode,
    val logMessage: String? = null,
) : RuntimeException(if (logMessage.isNullOrEmpty()) errorCode.message else logMessage)

enum class SmeemErrorCode(
    val code: Int,
    val message: String,
) {
    SYSTEM_ERROR(0, "시스템 오류가 발생했어요"),
    NETWORK_ERROR(1, "인터넷 연결을 확인해 주세요"),
    NETWORK_LOAD_ERROR(2, "데이터를 불러올 수 없어요"),
    CLIENT_ERROR(3, "잘못된 접근입니다."),
    FORBIDDEN(4, "권한이 필요한 접근입니다."),
    UNAUTHORIZED(5, "인증이 필요한 접근입니다."),
    UNKNOWN_ERROR(6, "알 수 없는 에러가 발생했습니다."),
    BOOKMARK_EXPRESSION_FAILED(7, "AI 표현 추출에 실패했어요.\n아직 본문이 충분한 콘텐츠만 지원하고 있어요."),
    BOOKMARK_DAILY_LIMIT_EXCEEDED(8, "AI 표현 추출에 실패했어요.\n하루 최대 10개까지만 불러올 수 있어요. 내일 만나요!")
}

fun SmeemException.description() = this.errorCode.message
fun SmeemException.logging(tag: String) = Log.e(tag, this.logMessage ?: this.description())
