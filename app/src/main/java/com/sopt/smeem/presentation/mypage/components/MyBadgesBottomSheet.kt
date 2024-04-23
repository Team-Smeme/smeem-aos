package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sopt.smeem.R
import com.sopt.smeem.data.datasource.BadgeList
import com.sopt.smeem.domain.model.mypage.MyBadges
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray500
import com.sopt.smeem.presentation.compose.theme.white
import com.sopt.smeem.util.VerticalSpacer
import com.sopt.smeem.util.previewPlaceholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBadgesBottomSheet(
    badge: MyBadges,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        modifier = modifier.fillMaxWidth(),
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        containerColor = white,
        dragHandle = null,
        onDismissRequest = onDismiss
    ) {
        if (badge.hasObtained) {
            ObtainedBottomSheetContent(info = badge, onDismiss = onDismiss)
        } else {
            NotObtainedBottomSheetContent(info = badge, onDismiss = onDismiss)
        }
    }
}

@Composable
fun ObtainedBottomSheetContent(
    info: MyBadges,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 57.dp)
            .clickable { onDismiss() },
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
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(info.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            placeholder = previewPlaceholder(image = R.drawable.ic_badge_welcome),
            modifier = Modifier
                .widthIn(max = 120.dp)
                .aspectRatio(1f)
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

@Composable
fun NotObtainedBottomSheetContent(
    info: MyBadges,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 57.dp)
            .clickable { onDismiss() },
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
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun ObtainedBottomSheetContentPreview() {
    ObtainedBottomSheetContent(info = BadgeList.sprint2.first(), onDismiss = {})
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun NotObtainedBottomSheetContentPreview() {
    NotObtainedBottomSheetContent(
        info = BadgeList.sprint2[2],
        onDismiss = {},
        modifier = Modifier.widthIn(max = 120.dp)
    )
}