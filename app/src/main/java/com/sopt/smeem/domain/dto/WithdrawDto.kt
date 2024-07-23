package com.sopt.smeem.domain.dto

import com.sopt.smeem.domain.model.WithdrawType

data class WithdrawDto(
    val type: WithdrawType,
    val reason: String = ""
)