package com.sopt.smeem.util

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource

@Composable
fun previewPlaceholder(@DrawableRes image: Int) =
    if (LocalInspectionMode.current) painterResource(id = image)
    else null