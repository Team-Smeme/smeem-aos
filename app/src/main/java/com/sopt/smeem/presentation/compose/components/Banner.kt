package com.sopt.smeem.presentation.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.gray200
import com.sopt.smeem.util.HorizontalSpacer
import com.sopt.smeem.util.VerticalSpacer
import com.sopt.smeem.util.noRippleClickable

@Composable
fun Banner(
    title: String,
    content: String,
    onBannerClick: () -> Unit,
    onBannerClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .background(gray100, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .clickable { onBannerClick() }
        ,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier =
                    Modifier
                        .padding(top = 20.dp, bottom = 20.dp, start = 14.dp),
            ) {
                Text(
                    text = title,
                    style = Typography.titleLarge,
                    color = black,
                )

                VerticalSpacer(height = 4.dp)

                Text(
                    text = content,
                    style = Typography.labelLarge,
                    color = black,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.ic_x),
                contentDescription = "close banner",
                tint = gray200,
                modifier = Modifier.noRippleClickable { onBannerClose() }
            )

            HorizontalSpacer(width = 8.dp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BannerPreview() {
    Banner(
        title = "Title",
        content = "Content",
        onBannerClick = {},
        onBannerClose = {},
        modifier = Modifier.padding(horizontal = 18.dp),
    )
}
