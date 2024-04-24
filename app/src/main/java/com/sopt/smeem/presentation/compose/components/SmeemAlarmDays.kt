package com.sopt.smeem.presentation.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.gray400
import com.sopt.smeem.presentation.compose.theme.white

@Composable
fun SmeemAlarmDays(
    modifier: Modifier = Modifier,
) {

    val daysOfWeek = Day.entries.map { it.korean }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = with(LocalConfiguration.current) {
        (screenWidth - 38.dp) / daysOfWeek.size
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
            .background(white)
            .border(1.dp, gray100, RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
    ) {

        items(daysOfWeek.size) { day ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(itemWidth),
            ) {
                Text(
                    text = daysOfWeek[day],
                    style = Typography.bodySmall,
                    color = gray400,
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SmeemAlarmDaysPreview() {
    SmeemAlarmDays()
}