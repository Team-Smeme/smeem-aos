package com.sopt.smeem.data.model.request

import com.sopt.smeem.domain.model.WithdrawType

data class WithdrawRequest(
    val withdrawType: WithdrawType,
    val reason: String?
)