package com.sopt.smeem.presentation.mypage.more

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.R
import com.sopt.smeem.presentation.theme.Typography
import com.sopt.smeem.presentation.theme.black
import com.sopt.smeem.presentation.theme.gray600
import com.sopt.smeem.util.VerticalSpacer
import com.sopt.smeem.util.noRippleClickable

@Composable
fun MoreScreen(
    navController: NavController,
    modifier: Modifier
) {
    val viewModel: MoreViewModel = hiltViewModel()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {

        VerticalSpacer(height = 30.dp)

        Text(
            text = stringResource(R.string.my_page_more_information),
            style = Typography.titleMedium,
            color = black
        )

        VerticalSpacer(height = 9.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable {
                    CustomTabsIntent
                        .Builder()
                        .build()
                        .run {
                            launchUrl(
                                context, Uri.parse(
                                    context.getString(R.string.my_page_more_manual_link)
                                )
                            )
                        }
                }
                .padding(vertical = 12.dp, horizontal = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.my_page_more_manual),
                style = Typography.bodyMedium,
                color = gray600
            )
        }

        VerticalSpacer(height = 60.dp)

        Text(
            text = stringResource(R.string.my_page_more_account_management),
            style = Typography.titleMedium,
            color = black
        )

        VerticalSpacer(height = 10.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.my_page_more_logout),
                style = Typography.bodyMedium,
                color = gray600
            )
        }

        VerticalSpacer(height = 6.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.my_page_more_delete_account),
                style = Typography.bodyMedium,
                color = gray600
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MoreScreenPreview() {
    MoreScreen(navController = rememberNavController(), modifier = Modifier)
}