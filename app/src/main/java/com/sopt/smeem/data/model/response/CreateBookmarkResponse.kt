package com.sopt.smeem.data.model.response

data class CreateBookmarkResponse(
    val scrapContent: ScrapContent,
    val expression: String,
    val translatedExpression: String,
    val scrapedCountPerDay: Int
) {
    data class ScrapContent(
        val thumbnail: String,
        val url: String,
        val description: String,
        val scrapType: String
    )
}