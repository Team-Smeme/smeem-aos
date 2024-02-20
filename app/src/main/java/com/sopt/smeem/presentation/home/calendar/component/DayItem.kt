package com.sopt.smeem.presentation.home.calendar.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sopt.smeem.presentation.home.calendar.ui.theme.SmeemTheme
import com.sopt.smeem.presentation.home.calendar.ui.theme.gray400
import java.time.LocalDate

@Composable
fun DayItem(
    date: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isCurrentMonth: Boolean = true,
    isDiaryWritten: Boolean = true,
    isFirstWeek: Boolean = true,
    isSixWeeks: Boolean = false,
) {
    val isToday = date == LocalDate.now()
    CompositionLocalProvider(
        LocalRippleTheme provides NoRippleTheme,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .padding(
                    top = when {
                        isFirstWeek -> 0.dp
                        isSixWeeks -> 13.6.dp
                        else -> 28.dp
                    },
                ),
        ) {
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        isToday -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.background
                    },
                ),
                border = BorderStroke(
                    1.dp,
                    if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                ),
                modifier = modifier
                    .aspectRatio(1f)
                    .alpha(if (!isCurrentMonth) 0f else 1f)
                    .clickable(
                        enabled = isCurrentMonth,
                        onClick = { onDayClick(date) },
                    )
                    .indication(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.titleSmall.copy(letterSpacing = (-0.72).sp),
                        color = when {
                            isToday -> MaterialTheme.colorScheme.onPrimary
                            isSelected || isDiaryWritten -> MaterialTheme.colorScheme.onBackground
                            else -> gray400
                        },
                    )
                }
            }
        }
    }
}

private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha() = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}

@Preview
@Composable
fun DayItemPreview() {
    SmeemTheme {
        DayItem(
            date = LocalDate.now().plusDays(0),
            onDayClick = {},
            isSelected = false,
            isCurrentMonth = true,
            isDiaryWritten = false,
            isFirstWeek = true,
            modifier = Modifier.widthIn(max = 36.dp),
        )
    }
}
