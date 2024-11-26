package com.sopt.smeem.presentation.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.sopt.smeem.domain.dto.CorrectionDto
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray400
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.white

@Composable
fun HighlightedDiary(
    allCorrections: List<CorrectionDto>,
    correctedCorrections: List<CorrectionDto>,
    currentPage: Int
): AnnotatedString {

    val defaultStyle = SpanStyle(color = black)
    val highlightStyle = SpanStyle(background = point, color = white)
    val otherStyle = SpanStyle(color = gray400)

    return buildAnnotatedString {
        allCorrections.forEach { correction ->
            val style = when {
                correctedCorrections.getOrNull(currentPage) == correction && correction.isCorrected -> highlightStyle
                else -> if (correctedCorrections.isNotEmpty()) otherStyle else defaultStyle
            }

            withStyle(style = style) {
                append(correction.originalSentence)
            }
            append(" ")
        }
    }
}
