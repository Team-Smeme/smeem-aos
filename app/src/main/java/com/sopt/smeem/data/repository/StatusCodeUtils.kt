package com.sopt.smeem.data.repository

import com.sopt.smeem.domain.common.SmeemErrorCode
import com.sopt.smeem.domain.common.SmeemException

fun Int.handleStatusCode(specificAction: (() -> Throwable)? = null): Throwable =
    specificAction?.invoke()
        ?: if (this in 500..599) {
            throw SmeemException(SmeemErrorCode.NETWORK_ERROR)
        } else if (this == 401) {
            throw SmeemException(SmeemErrorCode.UNAUTHORIZED)
        } else if (this == 403) {
            throw SmeemException(SmeemErrorCode.FORBIDDEN)
        } else {
            throw SmeemException(SmeemErrorCode.CLIENT_ERROR)
        }