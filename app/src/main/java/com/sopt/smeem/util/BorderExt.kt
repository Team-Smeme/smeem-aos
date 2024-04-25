package com.sopt.smeem.util

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sopt.smeem.presentation.compose.theme.gray200

data class Border(val strokeWidth: Dp, val color: Color)

@Stable
fun Modifier.sideBorder(
    start: Border? = null,
    top: Border? = null,
    end: Border? = null,
    bottom: Border? = null,
    topEnd: Border? = null,
    topStart: Border? = null,
    bottomStart: Border? = null,
    bottomEnd: Border? = null
) =
    drawBehind {
        start?.let {
            drawStartBorder(it, shareTop = top != null, shareBottom = bottom != null)
        }
        top?.let {
            drawTopBorder(it, shareStart = start != null, shareEnd = end != null)
        }
        end?.let {
            drawEndBorder(it, shareTop = top != null, shareBottom = bottom != null)
        }
        bottom?.let {
            drawBottomBorder(border = it, shareStart = start != null, shareEnd = end != null)
        }
        topStart?.let {
            drawTopStartBorderWithRoundCorner(
                startBorder = start ?: Border(1.dp, gray200),
                topBorder = top ?: Border(1.dp, gray200),
                roundCornerSize = 6.dp
            )
        }
        topEnd?.let {
            drawTopAndEndBorderWithRoundCorner(
                topBorder = top ?: Border(1.dp, gray200),
                endBorder = end ?: Border(1.dp, gray200),
                roundCornerSize = 6.dp
            )
        }
        bottomStart?.let {
            drawBottomStartBorderWithRoundCorner(
                startBorder = start ?: Border(1.dp, gray200),
                bottomBorder = bottom ?: Border(1.dp, gray200),
                roundCornerSize = 6.dp
            )
        }
        bottomEnd?.let {
            drawBottomEndBorderWithRoundCorner(
                endBorder = end ?: Border(1.dp, gray200),
                bottomBorder = bottom ?: Border(1.dp, gray200),
                roundCornerSize = 6.dp
            )
        }
    }

private fun DrawScope.drawTopBorder(
    border: Border,
    shareStart: Boolean = true,
    shareEnd: Boolean = true
) {
    val strokeWidthPx = border.strokeWidth.toPx()
    if (strokeWidthPx == 0f) return
    drawPath(
        Path().apply {
            moveTo(0f, 0f)
            lineTo(if (shareStart) strokeWidthPx else 0f, strokeWidthPx)
            val width = size.width
            lineTo(if (shareEnd) width - strokeWidthPx else width, strokeWidthPx)
            lineTo(width, 0f)
            close()
        },
        color = border.color
    )
}

private fun DrawScope.drawBottomBorder(
    border: Border,
    shareStart: Boolean,
    shareEnd: Boolean
) {
    val strokeWidthPx = border.strokeWidth.toPx()
    if (strokeWidthPx == 0f) return
    drawPath(
        Path().apply {
            val width = size.width
            val height = size.height
            moveTo(0f, height)
            lineTo(if (shareStart) strokeWidthPx else 0f, height - strokeWidthPx)
            lineTo(if (shareEnd) width - strokeWidthPx else width, height - strokeWidthPx)
            lineTo(width, height)
            close()
        },
        color = border.color
    )
}

private fun DrawScope.drawStartBorder(
    border: Border,
    shareTop: Boolean = true,
    shareBottom: Boolean = true
) {
    val strokeWidthPx = border.strokeWidth.toPx()
    if (strokeWidthPx == 0f) return
    drawPath(
        Path().apply {
            moveTo(0f, 0f)
            lineTo(strokeWidthPx, if (shareTop) strokeWidthPx else 0f)
            val height = size.height
            lineTo(strokeWidthPx, if (shareBottom) height - strokeWidthPx else height)
            lineTo(0f, height)
            close()
        },
        color = border.color
    )
}

private fun DrawScope.drawEndBorder(
    border: Border,
    shareTop: Boolean = true,
    shareBottom: Boolean = true
) {
    val strokeWidthPx = border.strokeWidth.toPx()
    if (strokeWidthPx == 0f) return
    drawPath(
        Path().apply {
            val width = size.width
            val height = size.height
            moveTo(width, 0f)
            lineTo(width - strokeWidthPx, if (shareTop) strokeWidthPx else 0f)
            lineTo(width - strokeWidthPx, if (shareBottom) height - strokeWidthPx else height)
            lineTo(width, height)
            close()
        },
        color = border.color
    )
}

private fun DrawScope.drawTopStartBorderWithRoundCorner(
    startBorder: Border,
    topBorder: Border,
    roundCornerSize: Dp = 6.dp
) {
    val topStrokeWidthPx = topBorder.strokeWidth.toPx() * 2
    val startStrokeWidthPx = startBorder.strokeWidth.toPx() * 2
    if (topStrokeWidthPx == 0f || startStrokeWidthPx == 0f) return

    val cornerRadiusPx = roundCornerSize.toPx()
    val width = size.width
    val height = size.height

    // 상단 테두리 그리기
    drawPath(
        path = Path().apply {
            moveTo(0f, cornerRadiusPx)
            quadraticBezierTo(
                x1 = 0f, y1 = 0f,
                x2 = cornerRadiusPx, y2 = 0f
            )
            lineTo(width, 0f)
        },
        color = topBorder.color,
        style = Stroke(width = topStrokeWidthPx)
    )

    // 왼쪽 테두리 그리기
    drawPath(
        path = Path().apply {
            moveTo(0f, cornerRadiusPx)
            lineTo(0f, height)
        },
        color = startBorder.color,
        style = Stroke(width = startStrokeWidthPx)
    )
}

private fun DrawScope.drawTopAndEndBorderWithRoundCorner(
    topBorder: Border,
    endBorder: Border,
    roundCornerSize: Dp = 6.dp
) {
    val topStrokeWidthPx = topBorder.strokeWidth.toPx() * 2
    val endStrokeWidthPx = endBorder.strokeWidth.toPx() * 2
    if (topStrokeWidthPx == 0f || endStrokeWidthPx == 0f) return

    val cornerRadiusPx = roundCornerSize.toPx()
    val width = size.width
    val height = size.height

    // 상단 테두리 그리기
    drawPath(
        path = Path().apply {
            moveTo(0f, 0f)
            lineTo(width - cornerRadiusPx, 0f) // 둥근 모서리 시작 직전까지 선 그리기
            quadraticBezierTo(
                x1 = width, y1 = 0f,
                x2 = width, y2 = cornerRadiusPx
            ) // 둥근 모서리 그리기
        },
        color = topBorder.color,
        style = Stroke(width = topStrokeWidthPx)
    )

    // 우측 테두리 그리기
    drawPath(
        path = Path().apply {
            moveTo(width, cornerRadiusPx) // 둥근 모서리가 끝난 지점부터 시작
            lineTo(width, height) // 하단까지 선 그리기
        },
        color = endBorder.color,
        style = Stroke(width = endStrokeWidthPx)
    )
}

private fun DrawScope.drawBottomStartBorderWithRoundCorner(
    startBorder: Border,
    bottomBorder: Border,
    roundCornerSize: Dp = 6.dp
) {
    val bottomStrokeWidthPx = bottomBorder.strokeWidth.toPx() * 2
    val startStrokeWidthPx = startBorder.strokeWidth.toPx() * 2
    if (bottomStrokeWidthPx == 0f || startStrokeWidthPx == 0f) return

    val cornerRadiusPx = roundCornerSize.toPx()
    val width = size.width

    // 하단 테두리 그리기
    drawPath(
        path = Path().apply {
            moveTo(0f, size.height - cornerRadiusPx)
            quadraticBezierTo(
                x1 = 0f, y1 = size.height,
                x2 = cornerRadiusPx, y2 = size.height
            )
            lineTo(width, size.height)
        },
        color = bottomBorder.color,
        style = Stroke(width = bottomStrokeWidthPx)
    )

    // 왼쪽 테두리 그리기
    drawPath(
        path = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, size.height - cornerRadiusPx)
        },
        color = startBorder.color,
        style = Stroke(width = startStrokeWidthPx)
    )
}

private fun DrawScope.drawBottomEndBorderWithRoundCorner(
    endBorder: Border,
    bottomBorder: Border,
    roundCornerSize: Dp = 6.dp
) {
    val bottomStrokeWidthPx = bottomBorder.strokeWidth.toPx() * 2
    val endStrokeWidthPx = endBorder.strokeWidth.toPx() * 2
    if (bottomStrokeWidthPx == 0f || endStrokeWidthPx == 0f) return

    val cornerRadiusPx = roundCornerSize.toPx()
    val width = size.width
    val height = size.height

    // 하단 테두리 그리기
    drawPath(
        path = Path().apply {
            moveTo(width - cornerRadiusPx, height)
            quadraticBezierTo(
                x1 = width, y1 = height,
                x2 = width, y2 = height - cornerRadiusPx
            ) // 둥근 모서리 그리기
            lineTo(width, 0f)
        },
        color = bottomBorder.color,
        style = Stroke(width = bottomStrokeWidthPx)
    )

    // 우측 테두리 그리기
    drawPath(
        path = Path().apply {
            moveTo(width, height - cornerRadiusPx) // 둥근 모서리가 끝난 지점부터 시작
            lineTo(width, 0f) // 상단까지 선 그리기
        },
        color = endBorder.color,
        style = Stroke(width = endStrokeWidthPx)
    )
}


