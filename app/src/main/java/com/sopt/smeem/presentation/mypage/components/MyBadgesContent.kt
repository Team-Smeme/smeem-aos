package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sopt.smeem.R
import com.sopt.smeem.data.datasource.BadgeList
import com.sopt.smeem.domain.dto.GetBadgeListDto
import com.sopt.smeem.presentation.compose.theme.SmeemTheme
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.util.previewPlaceholder

@Composable
fun MyBadgesContent(
    modifier: Modifier = Modifier,
    badges: List<GetBadgeListDto>,
    onClickCard: (GetBadgeListDto) -> Unit
) {
    SmeemContents(
        title = stringResource(R.string.my_badges)
    ) {
        LazyVerticalGrid(
            modifier = modifier.fillMaxWidth(),
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(7.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            items(badges) { badge ->
                if (badge.hasBadge) {
                    MyBadgesObtainedCard(info = badge, onClick = { onClickCard(badge) })
                } else {
                    MyBadgesNotObtainedCard(onClick = { onClickCard(badge) })
                }
            }
        }
    }
}

@Composable
fun MyBadgesObtainedCard(
    info: GetBadgeListDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(info.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    placeholder = previewPlaceholder(image = R.drawable.ic_badge_thirty_row)
                )
            }
            Text(
                text = info.name,
                style = Typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 9.sp
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun MyBadgesNotObtainedCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = gray100,
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxSize(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_badge_lock),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun MyBadgesPreview() {
    SmeemTheme {
        MyBadgesContent(badges = BadgeList.sprint2, onClickCard = {})
    }
}

@Preview(widthDp = 103)
@Composable
fun MyBadgesObtainedCardPreview() {
    SmeemTheme {
        MyBadgesObtainedCard(info = BadgeList.sprint2.first(), onClick = {})
    }
}

@Preview(widthDp = 103)
@Composable
fun MyBadgesNotObtainedCardPreview() {
    SmeemTheme {
        MyBadgesNotObtainedCard(onClick = {})
    }
}