package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.data.datasource.BadgeList
import com.sopt.smeem.domain.model.mypage.MyBadges
import com.sopt.smeem.presentation.theme.white
import com.sopt.smeem.presentation.theme.Typography
import com.sopt.smeem.presentation.theme.black
import com.sopt.smeem.presentation.theme.gray500
import com.sopt.smeem.util.VerticalSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBadgesBottomSheet(
    badge: MyBadges,
    modifier: Modifier = Modifier
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    ModalBottomSheet(
        modifier = modifier.fillMaxSize(),
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        containerColor = white,
        onDismissRequest = {}
    ) {
        MyBadgesBottomSheetContent(info = badge)
    }
}

@Composable
fun MyBadgesBottomSheetContent(
    info: MyBadges,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 57.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, end = 8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_badge_x),
                contentDescription = null
            )
        }
        Image(
            modifier = Modifier
                .widthIn(max = 120.dp)
                .aspectRatio(1f),
            painter = painterResource(id = R.drawable.ic_badge_welcome),
            contentDescription = null
        )
        VerticalSpacer(height = 26.dp)
        Text(
            text = info.name,
            style = Typography.headlineSmall,
            color = black
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = info.description,
            style = Typography.bodyMedium,
            color = black
        )
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = "${info.userPercentage}%의 사용자가 획득했어요",
            style = Typography.bodyMedium,
            color = gray500
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun MyBadgesBottomSheetContentPreview() {
    MyBadgesBottomSheetContent(info = BadgeList.sprint2.first())
}