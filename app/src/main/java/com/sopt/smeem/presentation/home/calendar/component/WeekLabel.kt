package com.sopt.smeem.presentation.home.calendar.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.presentation.home.calendar.ui.theme.SmeemTheme
import java.time.DayOfWeek

@Composable
fun WeekLabel(
    modifier: Modifier = Modifier,
    start: DayOfWeek = DayOfWeek.SUNDAY
) {
    val itemWidth = (LocalConfiguration.current.screenWidthDp.dp - 38.dp) / 7
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.padding(top = 16.dp, start = 18.dp, end = 18.dp, bottom = 7.dp)
    ) {
        val weekLabelArray = when (start) {
            DayOfWeek.MONDAY -> DayOfWeek.values()
            else -> arrayOf(
                DayOfWeek.SUNDAY,
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY
            )
        }
        val weekInitials = weekLabelArray.map { it.name.first().toString() }.toTypedArray()

        weekInitials.forEach { week ->
            Text(
                text = week,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(itemWidth)
            )
        }
    }
}

@Preview
@Composable
fun WeekLabelPreview() {
    SmeemTheme {
        WeekLabel()
    }
}