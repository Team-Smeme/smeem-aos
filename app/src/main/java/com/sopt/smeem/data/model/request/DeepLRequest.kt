package com.sopt.smeem.data.model.request

data class DeepLRequest(
    val text: List<String>,
    val source_lang: String,
    val target_lang: String,
)