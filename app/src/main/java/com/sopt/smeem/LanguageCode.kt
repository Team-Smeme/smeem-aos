package com.sopt.smeem

import com.sopt.smeem.domain.model.Language

enum class LanguageCode(val language: Language) {
    en(Language(type = "English", code = 1)),
    ko(Language(type = "Korean", code = 2,)),

}