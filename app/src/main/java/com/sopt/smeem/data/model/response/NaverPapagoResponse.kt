package com.sopt.smeem.data.model.response

data class NaverPapagoResponse(
    val message: PapageMessage
) {
    data class PapageMessage(
        val result: PapagoResult
    ) {
        data class PapagoResult(
            val srcLangType: String,
            val tarLangType: String,
            val translatedText: String,
        )
    }
}