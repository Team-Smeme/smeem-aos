package com.sopt.smeem.presentation.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = point,
    secondary = pointInactive,
    onPrimary = white,
    background = white,
    onBackground = black,
    surface = gray100,
    surfaceTint = white,
    onSurface = black
)

private val LightColorScheme = lightColorScheme(
    primary = point,
    secondary = pointInactive,
    onPrimary = white,
    background = white,
    onBackground = black,
    surface = gray100,
    surfaceTint = white,
    onSurface = black,
)

@Composable
fun SmeemTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}