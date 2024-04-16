package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.domain.model.mypage.MySmeem
import com.sopt.smeem.presentation.home.calendar.ui.theme.Typography
import com.sopt.smeem.presentation.home.calendar.ui.theme.gray100
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun MySmeemCard(
    modifier: Modifier = Modifier,
    mySmeem: MySmeem
) {
    SmeemContents(
        modifier = modifier.padding(vertical = 18.dp),
        title = stringResource(R.string.my_smeem)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors().copy(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, gray100)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MySmeemContent(
                    count = mySmeem.visitDays,
                    title = stringResource(R.string.my_plan_visit_days)
                )
                MySmeemContent(
                    count = mySmeem.diaryCount,
                    title = stringResource(R.string.my_plan_entire_diary)
                )
                MySmeemContent(
                    count = mySmeem.diaryComboCount,
                    title = stringResource(R.string.my_plan_continuity_diary)
                )
                MySmeemContent(
                    count = mySmeem.badgeCount,
                    title = stringResource(R.string.my_plan_badge)
                )
            }

        }
    }
}

@Composable
fun MySmeemContent(
    count: Int,
    title: String
) {
    Column(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            style = Typography.titleMedium, color = Color.Black
        )
        VerticalSpacer(height = 6.dp)
        Text(
            text = title, style = Typography.labelLarge, color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MySmeemContentPreview() {
    MySmeemContent(
        count = 23,
        title = "방문일"
    )
}

@Preview(showBackground = true, showSystemUi = true, widthDp = 360, heightDp = 800)
@Composable
fun MySmeemPreview() {
    MySmeemCard(
        mySmeem = MySmeem(
            visitDays = 23,
            diaryCount = 19,
            diaryComboCount = 3,
            badgeCount = 10
        )
    )
}
