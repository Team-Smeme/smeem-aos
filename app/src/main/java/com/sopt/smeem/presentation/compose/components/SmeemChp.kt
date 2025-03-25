package com.sopt.smeem.presentation.compose.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray200
import com.sopt.smeem.presentation.compose.theme.gray600
import com.sopt.smeem.presentation.compose.theme.point

@Composable
fun SmeemChp(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = Typography.labelMedium,
        color = if (isSelected) point else gray600,
        modifier = modifier
            .border(
                1.dp,
                color = if (isSelected) point else gray200,
                shape = RoundedCornerShape(6.dp)
            )
            .clip(RoundedCornerShape(6.dp))
            .padding(10.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectedSmeemChp() {
    SmeemChp(
        text = "이것은 스밈 칩이다",
        isSelected = true,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewUnselectedSmeemChp() {
    SmeemChp(
        text = "이것은 스밈 칩이다",
        isSelected = false,
    )
}