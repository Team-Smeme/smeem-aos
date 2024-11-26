package com.sopt.smeem.presentation.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray400
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.white
import com.sopt.smeem.util.HorizontalSpacer

@Composable
fun CoachBanner(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    val backgroundColor = if (isEnabled) point else gray400
    val clickModifier = if (isEnabled) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(5.dp))
            .then(clickModifier)
            .background(color = backgroundColor, shape = RoundedCornerShape(5.dp))
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_crown),
            contentDescription = "crown",
            tint = white
        )

        HorizontalSpacer(3.dp)

        Text(
            text = stringResource(R.string.coach_banner_comment),
            style = Typography.bodyLarge,
            color = white
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CoachBannerEnabledPreview() {
    CoachBanner(
        isEnabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CoachBannerDisabledPreview() {
    CoachBanner(
        isEnabled = false
    )
}
