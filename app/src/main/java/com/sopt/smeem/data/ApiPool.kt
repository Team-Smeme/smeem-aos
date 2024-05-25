package com.sopt.smeem.data

import com.sopt.smeem.domain.common.SmeemErrorCode
import com.sopt.smeem.domain.common.SmeemException
import retrofit2.HttpException

object ApiPool {
    @Deprecated("not used")
    fun <T> Result<T>.onHttpFailure(action: (SmeemException) -> Unit) {
        exceptionOrNull()?.let { exception ->
            when (exception) {
                is HttpException -> {
                    try {
                        when (exception.code()) {
                            400 -> throw SmeemException(
                                errorCode = SmeemErrorCode.SYSTEM_ERROR,
                            )

                            401 -> throw SmeemException(
                                errorCode = SmeemErrorCode.UNAUTHORIZED,
                            )

                            403 -> throw SmeemException(
                                errorCode = SmeemErrorCode.FORBIDDEN,
                            )

                            else -> throw SmeemException(
                                errorCode = SmeemErrorCode.UNKNOWN_ERROR,
                            )
                        }
                    } catch (e: SmeemException) {
                        action(e)
                    }
                }

                else -> action(
                    SmeemException(
                        errorCode = SmeemErrorCode.NETWORK_ERROR,
                        logMessage = "서버 통신에 실패했습니다.",
                    ),
                )
            }
        }
    }
}
