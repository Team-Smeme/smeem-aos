package com.sopt.smeem.domain.model

import androidx.annotation.DrawableRes
import com.sopt.smeem.LanguageCode
import com.sopt.smeem.R
import com.sopt.smeem.data.model.response.MyPageResponse

data class Language(
    val type: String = "English",
    val code: Long? = 1L,
    @DrawableRes val selected: Int = R.drawable.ic_checked
) {
    companion object {
        fun from(response: MyPageResponse): List<Language> {
            return listOf(Language(type = response.targetLang))
        }
    }
}
