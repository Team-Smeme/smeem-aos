package com.sopt.smeem.presentation.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.domain.model.Day
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray200
import com.sopt.smeem.presentation.compose.theme.gray500
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.white
import com.sopt.smeem.util.Border
import com.sopt.smeem.util.VerticalSpacer
import com.sopt.smeem.util.noRippleClickable
import com.sopt.smeem.util.sideBorder

@Composable
fun SmeemAlarmCard(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    selectedDays: MutableSet<Day>,
    trainingTime: String = stringResource(R.string.default_training_time),
    onAlarmCardClick: () -> Unit = {},
    isContentClickable: Boolean = false,
    onDayClick: (Day) -> Unit = {},
    onTimeCardClick: () -> Unit = {}
) {

    val daysOfWeek = Day.entries.map { it.korean }

    // 화면의 가로 길이를 가져와서 요일의 개수로 나누어 요일의 너비를 계산
    val itemWidth = with(LocalConfiguration.current) {
        (screenWidthDp.dp - 38.dp) / daysOfWeek.size
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .noRippleClickable { if (isActive && !isContentClickable) onAlarmCardClick() }
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .background(white)
        ) {
            items(daysOfWeek.size) { day ->
                val daySelected = selectedDays.contains(Day.from(daysOfWeek[day]))
                val borderColor = if (daySelected) point else gray200
                val sideBorder = Border(strokeWidth = 1.dp, color = borderColor)

                val radiusModifier = when (day) {
                    0 -> Modifier.clip(RoundedCornerShape(topStart = 6.dp))
                    daysOfWeek.size - 1 -> Modifier.clip(RoundedCornerShape(topEnd = 6.dp))
                    else -> Modifier
                }

                val itemModifier = Modifier
                    .size(itemWidth)
                    .then(radiusModifier)
                    .background(
                        when {
                            daySelected && isActive -> point
                            daySelected -> gray200
                            else -> white
                        }
                    )
                    .then(
                        if (!daySelected) {
                            Modifier
                                .let { modifier ->
                                    when (day) {
                                        0 -> {
                                            modifier.sideBorder(
                                                topStart = sideBorder,
                                                bottom = sideBorder
                                            )
                                        }

                                        daysOfWeek.size - 1 -> {
                                            modifier.sideBorder(
                                                topEnd = sideBorder,
                                                bottom = sideBorder
                                            )
                                        }

                                        else -> modifier.sideBorder(
                                            top = sideBorder,
                                            bottom = sideBorder
                                        )
                                    }
                                }
                                .padding(1.dp)
                        } else Modifier
                    )


                Box(
                    contentAlignment = Alignment.Center,
                    modifier = itemModifier.noRippleClickable { onDayClick(Day.from(daysOfWeek[day])) }
                ) {
                    Text(
                        text = daysOfWeek[day],
                        style = Typography.bodySmall,
                        color = if (daySelected) white else gray500,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
                .background(white)
                .sideBorder(
                    bottomStart = Border(1.dp, gray200),
                    bottomEnd = Border(1.dp, gray200)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isContentClickable) {
                            Modifier.noRippleClickable { onTimeCardClick() }
                        } else Modifier
                    )
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.my_page_setting_training_time_title),
                    style = Typography.labelLarge,
                    color = if (isActive) point else gray200
                )

                VerticalSpacer(height = 4.dp)

                Text(
                    text = trainingTime,
                    style = Typography.titleMedium,
                    color = if (isActive) point else gray200
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SmeemAlarmDaysPreview() {
    SmeemAlarmCard(
        modifier = Modifier.padding(horizontal = 19.dp),
        isActive = true,
        selectedDays = mutableSetOf(Day.MON, Day.TUE, Day.WED, Day.THU, Day.FRI),
        onAlarmCardClick = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InActiveSmeemAlarmDaysPreview() {
    SmeemAlarmCard(
        modifier = Modifier.padding(horizontal = 19.dp),
        isActive = false,
        selectedDays = mutableSetOf(Day.MON, Day.TUE, Day.WED, Day.THU, Day.FRI),
        onAlarmCardClick = {}
    )
}

