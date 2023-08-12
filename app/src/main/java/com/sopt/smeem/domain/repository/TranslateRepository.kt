package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.TranslateResult

interface TranslateRepository {
    suspend fun execute(text: String): TranslateResult
}