package com.sopt.smeem.presentation.compose.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray200
import com.sopt.smeem.presentation.compose.theme.gray600
import com.sopt.smeem.presentation.compose.theme.point


@Composable
fun SmeemChip(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    val rippleColor = if (isSelected) gray200 else point

    Surface(
        modifier = modifier
            .clip(shape = RoundedCornerShape(6.dp))
            .border(
                1.dp,
                color = if (isSelected) point else gray200,
                shape = RoundedCornerShape(6.dp)
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) { onClick() }
                } else Modifier
            )
            .indication(
                interactionSource = interactionSource,
                indication = rememberRipple(color = rippleColor)
            ),
        color = Color.Transparent,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            style = Typography.labelMedium,
            color = if (isSelected) point else gray600,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectedSmeemChp() {
    SmeemChip(
        text = "이것은 스밈 칩이다",
        isSelected = true,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewUnselectedSmeemChp() {
    SmeemChip(
        text = "이것은 스밈 칩이다",
        isSelected = false,
    )
}