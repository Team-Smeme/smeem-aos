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
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.white

@Composable
fun SmeemAlarmCard(
    modifier: Modifier = Modifier,
    isDaySelected: (String) -> Boolean
) {

    val daysOfWeek = Day.entries.map { it.korean }

    // 화면의 가로 길이를 가져와서 요일의 개수로 나누어 요일의 너비를 계산
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
                modifier = Modifier
                    .size(itemWidth)
                    .then(if (isDaySelected(daysOfWeek[day])) Modifier.background(point) else Modifier)
            ) {
                if (isDaySelected(daysOfWeek[day])) {
                    Text(
                        text = daysOfWeek[day],
                        style = Typography.bodySmall,
                        color = white,
                    )
                } else {
                    Text(
                        text = daysOfWeek[day],
                        style = Typography.bodySmall,
                        color = gray400,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SmeemAlarmDaysPreview() {
    val mockDays = listOf("월", "화", "수", "목", "금")
    SmeemAlarmCard(modifier = Modifier, isDaySelected = { mockDays.contains(it) })
}