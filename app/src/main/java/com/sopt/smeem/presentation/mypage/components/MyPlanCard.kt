package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.domain.dto.MyPlanDto
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.gray600
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.white
import com.sopt.smeem.util.VerticalSpacer
import com.sopt.smeem.util.dashedBorder

@Composable
fun MyPlanCard(
    modifier: Modifier = Modifier,
    myPlan: MyPlanDto
) {
    SmeemContents(
        modifier = modifier,
        title = stringResource(R.string.my_plan)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors().copy(
                containerColor = white
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, gray100)
        ) {

            if (myPlan.clearCount >= 5) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 20.dp)
                ) {
                    Text(
                        text = myPlan.plan,
                        style = Typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = black
                    )

                    VerticalSpacer(height = 16.dp)

                    MyPlanClearedDots(
                        modifier = Modifier.fillMaxWidth(),
                        clearedCount = myPlan.clearedCount,
                        clearCount = myPlan.clearCount
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp, bottom = 19.dp, start = 17.dp, end = 17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = myPlan.plan,
                        style = Typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = black,
                        modifier = Modifier.weight(2f)
                    )

                    MyPlanClearedDots(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        clearedCount = myPlan.clearedCount,
                        clearCount = myPlan.clearCount
                    )
                }
            }


        }
    }

}

@Composable
fun MyPlanDot(
    number: Int,
    isCleared: Boolean
) {

    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = if (isCleared) point else white
        ),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .size(20.dp)
            .then(
                if (!isCleared) Modifier.dashedBorder(1f, gray600, 5f, 5f) else Modifier
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(top = 3.dp, bottom = 3.dp, start = 5.dp, end = 6.dp),
                text = number.toString(),
                style = Typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = if (isCleared) white else gray600,
            )
        }
    }
}

@Composable
fun MyPlanClearedDots(
    modifier: Modifier = Modifier,
    clearedCount: Int,
    clearCount: Int
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = if (clearCount == 1) Arrangement.End else Arrangement.SpaceBetween
    ) {
        itemsIndexed(List(clearCount) { it }) { index, _ ->
            MyPlanDot(
                number = index + 1,
                isCleared = index < clearedCount
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPlanClearedDotPreview() {
    MyPlanDot(
        number = 1,
        isCleared = true
    )
}

@Preview(showBackground = true)
@Composable
fun MyPlanNotClearedDotPreview() {
    MyPlanDot(
        number = 3,
        isCleared = false
    )
}

@Preview(showBackground = true, name = "주 7회", group = "MyPlanCards")
@Composable
fun LargeMyPlanCardPreview() {
    MyPlanCard(
        myPlan = MyPlanDto(
            plan = "매일 일기 작성하기",
            goal = "유창한 비즈니스 영어",
            clearedCount = 3,
            clearCount = 7
        )
    )
}

@Preview(showBackground = true, name = "주 5회", group = "MyPlanCards")
@Composable
fun MediumMyPlanCardPreview() {
    MyPlanCard(
        myPlan = MyPlanDto(
            plan = "매일 일기 작성하기",
            goal = "유창한 비즈니스 영어",
            clearedCount = 3,
            clearCount = 5
        )
    )
}

@Preview(showBackground = true, name = "주 3회", group = "MyPlanCards")
@Composable
fun SmallMyPlanCardPreview() {
    MyPlanCard(
        myPlan = MyPlanDto(
            plan = "매일 일기 작성하기",
            goal = "유창한 비즈니스 영어",
            clearedCount = 3,
            clearCount = 3
        )
    )
}

@Preview(showBackground = true, name = "주 1회", group = "MyPlanCards")
@Composable
fun ExtraSmallMyPlanCardPreview() {
    MyPlanCard(
        myPlan = MyPlanDto(
            plan = "매일 일기 작성하기",
            goal = "유창한 비즈니스 영어",
            clearedCount = 1,
            clearCount = 1
        )
    )
}


