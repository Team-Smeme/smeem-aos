package com.sopt.smeem.data

import com.sopt.smeem.SmeemErrorCode
import com.sopt.smeem.SmeemException
import retrofit2.HttpException

object ApiPool {
    fun <T> Result<T>.onHttpFailure(action: (SmeemException) -> Unit) {
        exceptionOrNull()?.let { exception ->
            when (exception) {
                is HttpException -> {
                    try {
                        when (exception.code()) {
                            400 -> throw SmeemException(
                                errorCode = SmeemErrorCode.SYSTEM_ERROR,
                                throwable = exception,
                            )

                            401 -> throw SmeemException(
                                errorCode = SmeemErrorCode.UNAUTHORIZED,
                                throwable = exception,
                            )

                            403 -> throw SmeemException(
                                errorCode = SmeemErrorCode.FORBIDDEN,
                                throwable = exception,
                            )

                            else -> throw SmeemException(
                                errorCode = SmeemErrorCode.UNKNOWN_ERROR,
                                throwable = exception,
                            )
                        }
                    } catch (e: SmeemException) {
                        action(e)
                    }
                }

                else -> action(
                    SmeemException(
                        errorCode = SmeemErrorCode.NETWORK_ERROR,
                        throwable = exception,
                        logMessage = "서버 통신에 실패했습니다.",
                    ),
                )
            }
        }
    }
}
