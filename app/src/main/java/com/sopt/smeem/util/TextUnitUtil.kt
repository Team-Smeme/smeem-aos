package com.sopt.smeem.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * `Dp` 값을 `TextUnit`으로 변환합니다.
 *
 * @return 변환된 `TextUnit` 값입니다.
 */
@Composable
fun Dp.toTextDp(): TextUnit = textSp(density = LocalDensity.current)

/**
 * `Dp` 값을 `TextUnit`으로 변환합니다.
 *
 * @param density 변환에 사용할 `Density` 객체입니다.
 * @return 변환된 `TextUnit` 값입니다.
 */
private fun Dp.textSp(density: Density): TextUnit = with(density) {
    this@textSp.toSp()
}
