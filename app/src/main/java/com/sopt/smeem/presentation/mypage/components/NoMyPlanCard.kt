package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.gray400
import com.sopt.smeem.presentation.compose.theme.gray500
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun NoMyPlanCard(
    modifier: Modifier = Modifier,
    onSetPlanClick: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = gray100, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable { onSetPlanClick() }
            .padding(vertical = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.no_plan_title),
            style = Typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = gray500
        )

        VerticalSpacer(height = 6.dp)

        Text(
            text = stringResource(R.string.no_plan_navigate_description),
            style = Typography.labelLarge,
            color = gray400,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NoMyPlanCardPreview() {
    NoMyPlanCard(modifier = Modifier) {}
}