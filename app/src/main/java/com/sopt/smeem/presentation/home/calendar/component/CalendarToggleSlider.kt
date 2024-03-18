package com.sopt.smeem.presentation.home.calendar.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.sopt.smeem.presentation.home.calendar.ui.theme.gray300

@Composable
fun CalendarToggleSlider(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            drawLine(
                start = Offset(x = canvasWidth / 2 - 36.dp.toPx(), y = canvasHeight / 2),
                end = Offset(x = canvasWidth / 2 + 36.dp.toPx(), y = canvasHeight / 2),
                color = gray300,
                cap = StrokeCap.Round,
                strokeWidth = 4.dp.toPx(),
            )
        }
    }
}