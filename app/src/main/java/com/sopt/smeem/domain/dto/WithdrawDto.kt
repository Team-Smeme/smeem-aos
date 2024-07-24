package com.sopt.smeem.domain.dto

import com.sopt.smeem.domain.model.WithdrawType
import com.sopt.smeem.presentation.mypage.REASON_MAX_LENGTH
import com.sopt.smeem.presentation.mypage.REASON_MIN_LENGTH

data class WithdrawDto(
    val type: WithdrawType,
    val reason: String? = null
) {
    fun isValidContent(): Boolean {
        return if (type == WithdrawType.ETC) isValidReason() else true
    }

    private fun isValidReason(): Boolean =
        reason?.length in REASON_MIN_LENGTH..REASON_MAX_LENGTH
}