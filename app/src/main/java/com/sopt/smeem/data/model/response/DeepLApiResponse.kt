package com.sopt.smeem.data.model.response

data class DeepLApiResponse(
    val translations: List<TranslateResult>,
) {
    data class TranslateResult(
        val text: String,
    )
}