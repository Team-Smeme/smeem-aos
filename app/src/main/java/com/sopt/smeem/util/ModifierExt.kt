package com.sopt.smeem.util

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.dashedBorder(
    borderWidth: Float,
    color: Color,
    dashLength: Float,
    spaceLength: Float
): Modifier {
    return this.drawWithContent {
        drawContent()

        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, spaceLength), 0f)

        drawCircle(
            color = color,
            radius = size.minDimension / 2 - borderWidth / 2,
            style = Stroke(width = borderWidth, pathEffect = pathEffect)
        )
    }
}

fun Modifier.addFocusCleaner(focusManager: FocusManager): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
        })
    }
}
