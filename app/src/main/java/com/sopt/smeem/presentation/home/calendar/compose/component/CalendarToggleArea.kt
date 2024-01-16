package com.sopt.smeem.presentation.home.calendar.compose.component

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.sopt.smeem.presentation.home.calendar.compose.ui.theme.gray100

@Composable
fun CalendarToggleArea(
    isExpanded: Boolean,
    expand: () -> Unit,
    collapse: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    min = when {
                        isExpanded -> 42.dp
                        else -> 20.dp
                    }
                )
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        if (dragAmount.y < 0) {
                            collapse()
                        } else if (dragAmount.y > 0) {
                            expand()
                        }
                    }
                }
        )
        Divider(
            color = gray100,
            thickness = 4.dp
        )
    }
}