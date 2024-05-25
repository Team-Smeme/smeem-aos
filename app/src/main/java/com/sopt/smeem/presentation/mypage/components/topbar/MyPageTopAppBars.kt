package com.sopt.smeem.presentation.mypage.components.topbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.background
import com.sopt.smeem.presentation.compose.theme.gray500
import com.sopt.smeem.presentation.compose.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySummaryTopAppBar(
    onNavigationIconClick: () -> Unit = {},
    onSettingClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
            containerColor = background
        ),
        title = {
            Text(
                text = "성과 요약",
                style = Typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(start = 6.dp),
                onClick = onNavigationIconClick
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier.padding(end = 16.dp),
                onClick = onSettingClick
            ) {
                Text(
                    text = "설정",
                    style = Typography.bodyMedium.copy(
                        color = gray500,
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTopAppBar(
    onNavigationIconClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
            containerColor = white
        ),
        title = {
            Text(
                text = "설정",
                style = Typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(start = 6.dp),
                onClick = onNavigationIconClick
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier.padding(end = 6.dp),
                onClick = onMoreClick
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_menu_more),
                    contentDescription = "More"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlyBackArrowTopAppBar(
    onNavigationIconClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
            containerColor = white
        ),
        title = {},
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(start = 6.dp),
                onClick = onNavigationIconClick
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back"
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTopAppbar(
    onNavigationIconClick: () -> Unit = {},
    title: String
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
            containerColor = white
        ),
        title = {
            Text(
                text = title,
                style = Typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(start = 6.dp),
                onClick = onNavigationIconClick
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back"
                )
            }
        },
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MySummaryTopAppBarPreview() {
    MySummaryTopAppBar()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingTopAppBarPreview() {
    SettingTopAppBar()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnlyBackArrowTopAppBarPreview() {
    OnlyBackArrowTopAppBar()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TitleTopAppbarPreview() {
    TitleTopAppbar(title = stringResource(R.string.my_page_change_nickname))
}