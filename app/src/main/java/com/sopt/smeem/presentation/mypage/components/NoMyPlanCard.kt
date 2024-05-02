package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            .padding(vertical = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "아직 플랜이 없어요!",
            style = Typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = gray500
        )

        VerticalSpacer(height = 6.dp)

        Text(
            text = "플랜 설정하러가기",
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