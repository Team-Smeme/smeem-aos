package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sopt.smeem.presentation.theme.Typography
import com.sopt.smeem.presentation.theme.black
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun SmeemContents(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        Text(
            text = title,
            style = Typography.titleLarge,
            color = black
        )

        VerticalSpacer(height = 12.dp)

        content()
    }
}