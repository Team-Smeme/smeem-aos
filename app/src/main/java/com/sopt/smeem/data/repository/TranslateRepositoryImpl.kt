package com.sopt.smeem.data.repository

import com.sopt.smeem.data.datasource.Translater
import com.sopt.smeem.domain.model.TranslateResult
import com.sopt.smeem.domain.repository.TranslateRepository

class TranslateRepositoryImpl(
    private val translater: Translater
) : TranslateRepository {
    override suspend fun execute(text: String): TranslateResult =
        TranslateResult(
            translateResult = translater.translateKo2En(text)
                .body()?.translations?.get(0)?.text ?: "network error"
        )

}